package essai.org;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import essai.org.network.MessageType;
import essai.org.network.NetworkMessage;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class WhatsAppClientConsole {
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private final ObjectMapper objectMapper = new ObjectMapper();
    {
        objectMapper.registerModule(new JavaTimeModule());
    }
    private String pseudo;
    private boolean connected = false;
    private Scanner scanner;

    public WhatsAppClientConsole() {
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("========================================");
        System.out.println("  Client WhatsApp Console - L3IAGE");
        System.out.println("========================================");
        System.out.println();

        // Demander le pseudo
        System.out.print("Entrez votre pseudo: ");
        this.pseudo = scanner.nextLine().trim();
        
        if (pseudo.isEmpty()) {
            System.out.println("❌ Pseudo invalide !");
            return;
        }

        // Se connecter au serveur
        System.out.println("Connexion au serveur...");
        
        try {
            socket = new Socket("localhost", 8080);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            
            // Envoyer le message de connexion
            NetworkMessage connectMsg = NetworkMessage.createConnect(pseudo);
            send(connectMsg);
            
            connected = true;
            System.out.println("✅ Connecté au serveur !");
            System.out.println("Tapez vos messages (tapez 'quit' pour quitter)");
            System.out.println("----------------------------------------");
            
            // Démarrer la réception des messages
            startMessageReceiver();
            
            // Boucle d'envoi de messages
            sendMessageLoop();
            
        } catch (IOException e) {
            System.out.println("❌ Impossible de se connecter au serveur !");
            System.out.println("Vérifiez que le serveur est démarré sur le port 8080");
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    private void send(NetworkMessage message) throws IOException {
        String json = objectMapper.writeValueAsString(message);
        out.write(json);
        out.newLine();
        out.flush();
    }

    private void startMessageReceiver() {
        Thread receiverThread = new Thread(() -> {
            try {
                String line;
                while (connected && (line = in.readLine()) != null) {
                    NetworkMessage msg = objectMapper.readValue(line, NetworkMessage.class);
                    handleMessage(msg);
                }
            } catch (IOException e) {
                if (connected) {
                    System.out.println("❌ Connexion perdue avec le serveur: " + e.getMessage());
                    connected = false;
                }
            }
        });
        receiverThread.setDaemon(true);
        receiverThread.start();
    }

    private void handleMessage(NetworkMessage msg) {
        switch (msg.getType()) {
            case SUCCESS -> System.out.println("✅ " + msg.getContent());
            case ERROR -> System.out.println("❌ " + msg.getErrorMessage());
            case RECEIVE_MESSAGE -> System.out.println("[" + msg.getSender() + "]: " + msg.getContent());
            case MEMBER_JOINED -> System.out.println("👋 " + msg.getSender() + " a rejoint le chat");
            case MEMBER_LEFT -> System.out.println("👋 " + msg.getSender() + " a quitté le chat");
            case MEMBER_BANNED -> {
                System.out.println("🚫 " + msg.getSender() + " a été banni: " + msg.getContent());
                if (msg.getSender().equals(pseudo)) {
                    System.out.println("❌ Vous avez été banni !");
                    connected = false;
                }
            }
            case HISTORY_RESPONSE -> {
                if (msg.getData() instanceof java.util.List<?> messages) {
                    System.out.println("📜 Historique des messages:");
                    for (Object m : messages) {
                        System.out.println("  " + m.toString());
                    }
                }
            }
            default -> {}
        }
    }

    private void sendMessageLoop() {
        while (connected) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
                continue;
            }
            
            if (input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("exit")) {
                System.out.println("Déconnexion...");
                try {
                    send(NetworkMessage.createDisconnect(pseudo));
                } catch (IOException e) {
                    System.out.println("Erreur lors de la déconnexion: " + e.getMessage());
                }
                connected = false;
                break;
            }
            
            // Envoyer le message
            try {
                send(NetworkMessage.createSendMessage(pseudo, input));
            } catch (IOException e) {
                System.out.println("❌ Erreur lors de l'envoi: " + e.getMessage());
            }
        }
        
        try {
            if (socket != null) socket.close();
        } catch (IOException ignored) {}
        
        System.out.println("Au revoir !");
    }

    public static void main(String[] args) {
        WhatsAppClientConsole client = new WhatsAppClientConsole();
        client.start();
    }
} 