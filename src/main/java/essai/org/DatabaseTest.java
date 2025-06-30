package essai.org;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseTest {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseTest.class);
    
    public static void main(String[] args) {
        logger.info("Test de connexion à la base de données...");
        
        try {
            // Créer la session factory
            SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
            logger.info("✅ SessionFactory créée avec succès");
            
            // Tester la connexion
            try (Session session = sessionFactory.openSession()) {
                logger.info("✅ Connexion à la base de données réussie");
                
                // Test simple de requête
                session.createQuery("FROM essai.org.model.Member").setMaxResults(1).list();
                logger.info("✅ Tables créées et accessibles");
                
                logger.info("🎉 Base de données configurée correctement !");
            }
            
            sessionFactory.close();
            
        } catch (Exception e) {
            logger.error("❌ Erreur lors du test de la base de données", e);
            System.out.println("\n🔧 Solutions possibles :");
            System.out.println("1. Vérifiez que MySQL est démarré");
            System.out.println("2. Vérifiez les paramètres de connexion dans hibernate.cfg.xml");
            System.out.println("3. Vérifiez que l'utilisateur a les droits sur whatsapp_db");
            System.out.println("4. Vérifiez que la base whatsapp_db existe");
        }
    }
} 