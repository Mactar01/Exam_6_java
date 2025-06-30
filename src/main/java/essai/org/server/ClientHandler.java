package essai.org.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import essai.org.model.Message;
import essai.org.model.Member;
import essai.org.network.MessageType;
import essai.org.network.NetworkMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Optional;

public class ClientHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);
    private final Socket socket;
    private final WhatsAppServer server;
    private String pseudo;
    private BufferedReader in;
    private BufferedWriter out;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private boolean running = true;

    public ClientHandler(Socket socket, WhatsAppServer server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // Authentification et enregistrement du pseudo
            String line = in.readLine();
            NetworkMessage connectMsg = objectMapper.readValue(line, NetworkMessage.class);
            if (connectMsg.getType() == MessageType.CONNECT) {
                pseudo = connectMsg.getSender();
                if (!server.isPseudoAvailable(pseudo)) {
                    sendMessage(NetworkMessage.createError("Pseudo déjà utilisé ou membre déjà connecté."));
                    closeConnection();
                    return;
                }
                if (!server.addMember(pseudo)) {
                    sendMessage(NetworkMessage.createError("Le groupe est plein (max 7 membres)."));
                    closeConnection();
                    return;
                }
                server.registerClient(pseudo, this);
                sendMessage(NetworkMessage.createSuccess("Connexion réussie."));

                // Envoyer l'historique des 15 derniers messages
                List<Message> lastMessages = server.getLastMessages(15);
                sendMessage(NetworkMessage.builder()
                        .type(MessageType.HISTORY_RESPONSE)
                        .data(lastMessages)
                        .build());
            } else {
                sendMessage(NetworkMessage.createError("Connexion invalide."));
                closeConnection();
                return;
            }

            // Boucle principale de réception des messages
            while (running && !socket.isClosed()) {
                String input = in.readLine();
                if (input == null) break;
                NetworkMessage received = objectMapper.readValue(input, NetworkMessage.class);

                switch (received.getType()) {
                    case SEND_MESSAGE -> {
                        server.handleMessage(pseudo, received.getContent());
                    }
                    case DISCONNECT -> {
                        running = false;
                        break;
                    }
                    case REQUEST_HISTORY -> {
                        List<Message> lastMessages = server.getLastMessages(15);
                        sendMessage(NetworkMessage.builder()
                                .type(MessageType.HISTORY_RESPONSE)
                                .data(lastMessages)
                                .build());
                    }
                    default -> sendMessage(NetworkMessage.createError("Type de message non supporté."));
                }
            }
        } catch (Exception e) {
            logger.error("Erreur dans ClientHandler pour {}", pseudo, e);
        } finally {
            closeConnection();
        }
    }

    public void sendMessage(NetworkMessage message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            out.write(json);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            logger.error("Erreur lors de l'envoi du message à {}", pseudo, e);
        }
    }

    private void closeConnection() {
        try {
            running = false;
            if (pseudo != null) {
                server.removeMember(pseudo);
                server.unregisterClient(pseudo);
            }
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
            logger.info("Connexion fermée pour {}", pseudo);
        } catch (IOException e) {
            logger.error("Erreur lors de la fermeture de la connexion pour {}", pseudo, e);
        }
    }
} 