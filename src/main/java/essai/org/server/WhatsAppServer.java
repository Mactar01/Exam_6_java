package essai.org.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import essai.org.dao.MemberDAO;
import essai.org.dao.MessageDAO;
import essai.org.model.Member;
import essai.org.model.Message;
import essai.org.network.MessageType;
import essai.org.network.NetworkMessage;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WhatsAppServer {
    
    private static final Logger logger = LoggerFactory.getLogger(WhatsAppServer.class);
    private static final int PORT = 8080;
    private static final int MAX_MEMBERS = 7;
    private static final List<String> BANNED_WORDS = Arrays.asList(
        "GENOCID", "TERRORISM", "ATTACK", "CHELSEA", "JAVA NEKHOUL"
    );
    
    private final ServerSocket serverSocket;
    private final SessionFactory sessionFactory;
    private final MemberDAO memberDAO;
    private final MessageDAO messageDAO;
    private final ObjectMapper objectMapper;
    private final ExecutorService executorService;
    private final Map<String, ClientHandler> connectedClients;
    private final Set<String> onlineMembers;
    
    public WhatsAppServer() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
        this.sessionFactory = new Configuration().configure().buildSessionFactory();
        this.memberDAO = new MemberDAO(sessionFactory);
        this.messageDAO = new MessageDAO(sessionFactory);
        this.objectMapper = new ObjectMapper();
        this.executorService = Executors.newCachedThreadPool();
        this.connectedClients = new ConcurrentHashMap<>();
        this.onlineMembers = ConcurrentHashMap.newKeySet();
        
        logger.info("Serveur WhatsApp démarré sur le port {}", PORT);
    }
    
    public void start() {
        try {
            while (!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                logger.info("Nouvelle connexion acceptée: {}", clientSocket.getInetAddress());
                
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                executorService.execute(clientHandler);
            }
        } catch (IOException e) {
            logger.error("Erreur lors de l'acceptation des connexions", e);
        }
    }
    
    public synchronized boolean addMember(String pseudo) {
        if (onlineMembers.size() >= MAX_MEMBERS) {
            return false;
        }
        
        if (onlineMembers.contains(pseudo)) {
            return false;
        }
        
        // Vérifier si le pseudo existe déjà en base
        if (memberDAO.findByPseudo(pseudo).isPresent()) {
            return false;
        }
        
        // Créer et sauvegarder le nouveau membre
        Member member = Member.builder()
                .pseudo(pseudo)
                .isOnline(true)
                .isBanned(false)
                .build();
        
        memberDAO.save(member);
        onlineMembers.add(pseudo);
        
        // Notifier tous les autres membres
        broadcastMessage(NetworkMessage.createMemberJoined(pseudo), pseudo);
        
        logger.info("Nouveau membre ajouté: {}", pseudo);
        return true;
    }
    
    public synchronized void removeMember(String pseudo) {
        onlineMembers.remove(pseudo);
        connectedClients.remove(pseudo);
        memberDAO.updateOnlineStatus(pseudo, false);
        
        // Notifier tous les autres membres
        broadcastMessage(NetworkMessage.createMemberLeft(pseudo), pseudo);
        
        logger.info("Membre supprimé: {}", pseudo);
    }
    
    public synchronized void banMember(String pseudo, String reason) {
        memberDAO.banMember(pseudo, reason);
        onlineMembers.remove(pseudo);
        connectedClients.remove(pseudo);
        
        // Notifier tous les autres membres
        broadcastMessage(NetworkMessage.createMemberBanned(pseudo, reason), null);
        
        logger.info("Membre banni: {} - Raison: {}", pseudo, reason);
    }
    
    public void broadcastMessage(NetworkMessage message, String excludeSender) {
        for (Map.Entry<String, ClientHandler> entry : connectedClients.entrySet()) {
            String pseudo = entry.getKey();
            ClientHandler handler = entry.getValue();
            
            if (!pseudo.equals(excludeSender)) {
                handler.sendMessage(message);
            }
        }
    }
    
    public void handleMessage(String sender, String content) {
        // Vérifier les mots interdits
        String upperContent = content.toUpperCase();
        for (String bannedWord : BANNED_WORDS) {
            if (upperContent.contains(bannedWord)) {
                banMember(sender, "Message contenant un mot interdit: " + bannedWord);
                return;
            }
        }
        
        // Sauvegarder le message en base
        Optional<Member> memberOpt = memberDAO.findByPseudo(sender);
        if (memberOpt.isPresent()) {
            Message message = Message.builder()
                    .content(content)
                    .sender(memberOpt.get())
                    .build();
            
            messageDAO.save(message);
            
            // Diffuser le message à tous les autres membres
            NetworkMessage networkMessage = NetworkMessage.createReceiveMessage(sender, content);
            broadcastMessage(networkMessage, sender);
            
            logger.info("Message diffusé de {}: {}", sender, content);
        }
    }
    
    public List<Message> getLastMessages(int limit) {
        return messageDAO.getLastMessages(limit);
    }
    
    public void registerClient(String pseudo, ClientHandler handler) {
        connectedClients.put(pseudo, handler);
    }
    
    public void unregisterClient(String pseudo) {
        connectedClients.remove(pseudo);
    }
    
    public boolean isPseudoAvailable(String pseudo) {
        return !onlineMembers.contains(pseudo) && memberDAO.findByPseudo(pseudo).isEmpty();
    }
    
    public int getOnlineMemberCount() {
        return onlineMembers.size();
    }
    
    public void shutdown() {
        try {
            executorService.shutdown();
            serverSocket.close();
            sessionFactory.close();
            logger.info("Serveur arrêté");
        } catch (IOException e) {
            logger.error("Erreur lors de l'arrêt du serveur", e);
        }
    }
    
    public static void main(String[] args) {
        try {
            WhatsAppServer server = new WhatsAppServer();
            server.start();
        } catch (IOException e) {
            logger.error("Impossible de démarrer le serveur", e);
        }
    }
} 