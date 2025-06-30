package essai.org.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import essai.org.controller.ClientController;
import essai.org.model.ChatMessage;

import java.io.*;
import java.net.Socket;

public class ClientNetwork {
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ClientController controller;
    private Thread listenThread;

    public ClientNetwork(ClientController controller) {
        this.controller = controller;
    }

    public void connect(String pseudo, String host, int port) {
        try {
            socket = new Socket(host, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // Envoyer le message de connexion
            NetworkMessage connectMsg = NetworkMessage.createConnect(pseudo);
            send(connectMsg);

            // Démarrer l'écoute des messages du serveur
            listenThread = new Thread(this::listen);
            listenThread.setDaemon(true);
            listenThread.start();
        } catch (IOException e) {
            controller.showError("Impossible de se connecter au serveur.");
        }
    }

    public void send(NetworkMessage message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            out.write(json);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            controller.showError("Erreur lors de l'envoi du message.");
        }
    }

    public void sendMessage(String pseudo, String content) {
        send(NetworkMessage.createSendMessage(pseudo, content));
    }

    private void listen() {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                NetworkMessage msg = objectMapper.readValue(line, NetworkMessage.class);
                handleMessage(msg);
            }
        } catch (IOException e) {
            controller.showError("Connexion perdue avec le serveur.");
        }
    }

    private void handleMessage(NetworkMessage msg) {
        switch (msg.getType()) {
            case SUCCESS -> controller.showError("Connecté !");
            case ERROR -> controller.showError(msg.getErrorMessage());
            case RECEIVE_MESSAGE -> controller.addMessage(ChatMessage.createUserMessage(msg.getSender(), msg.getContent()));
            case MEMBER_JOINED -> controller.addMessage(ChatMessage.createJoinMessage(msg.getSender()));
            case MEMBER_LEFT -> controller.addMessage(ChatMessage.createLeaveMessage(msg.getSender()));
            case MEMBER_BANNED -> {
                controller.addMessage(ChatMessage.createBanMessage(msg.getSender(), msg.getContent()));
                if (msg.getSender().equals(controller.getPseudo())) {
                    controller.showError("Vous avez été banni !");
                }
            }
            case HISTORY_RESPONSE -> {
                if (msg.getData() instanceof java.util.List<?> messages) {
                    for (Object m : messages) {
                        controller.addMessage("Historique: " + m.toString());
                    }
                }
            }
            default -> {}
        }
    }

    public void disconnect(String pseudo) {
        send(NetworkMessage.createDisconnect(pseudo));
        try {
            if (socket != null) socket.close();
        } catch (IOException ignored) {}
    }
} 