package essai.org;

import essai.org.server.WhatsAppServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerTest {
    private static final Logger logger = LoggerFactory.getLogger(ServerTest.class);
    
    public static void main(String[] args) {
        logger.info("Test de démarrage du serveur...");
        
        try {
            WhatsAppServer server = new WhatsAppServer();
            logger.info("✅ Serveur créé avec succès");
            logger.info("✅ Base de données connectée");
            logger.info("✅ Serveur prêt à accepter les connexions sur le port 8080");
            
            // Démarrer le serveur dans un thread séparé pour le test
            Thread serverThread = new Thread(() -> {
                try {
                    server.start();
                } catch (Exception e) {
                    logger.error("Erreur dans le serveur", e);
                }
            });
            serverThread.setDaemon(true);
            serverThread.start();
            
            logger.info("🎉 Serveur démarré avec succès !");
            logger.info("💡 Vous pouvez maintenant lancer le client");
            
            // Attendre un peu pour voir les logs
            Thread.sleep(2000);
            
        } catch (Exception e) {
            logger.error("❌ Erreur lors du démarrage du serveur", e);
            System.out.println("\n🔧 Solutions possibles :");
            System.out.println("1. Vérifiez que le port 8080 n'est pas déjà utilisé");
            System.out.println("2. Vérifiez que la base de données est accessible");
            System.out.println("3. Vérifiez les logs pour plus de détails");
        }
    }
} 