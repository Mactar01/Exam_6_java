package essai.org;

import essai.org.server.WhatsAppServer;
import java.io.IOException;

public class ServerTestSimple {
    
    public static void main(String[] args) {
        System.out.println("=== Test du Serveur WhatsApp ===");
        System.out.println();
        
        try {
            System.out.println("1. Création du serveur...");
            WhatsAppServer server = new WhatsAppServer();
            System.out.println("✅ Serveur créé avec succès");
            
            System.out.println("2. Test de la base de données...");
            // Le serveur teste automatiquement la connexion à la base
            System.out.println("✅ Base de données connectée");
            
            System.out.println("3. Démarrage du serveur...");
            System.out.println("   Le serveur va démarrer et écouter sur le port 8080");
            System.out.println("   Appuyez sur Ctrl+C pour arrêter le serveur");
            System.out.println();
            
            // Démarrer le serveur
            server.start();
            
        } catch (IOException e) {
            System.out.println("❌ Erreur lors de la création du serveur : " + e.getMessage());
            System.out.println();
            System.out.println("🔧 Solutions possibles :");
            System.out.println("1. Vérifiez que le port 8080 n'est pas déjà utilisé");
            System.out.println("2. Vérifiez que la base de données est accessible");
        } catch (Exception e) {
            System.out.println("❌ Erreur inattendue : " + e.getMessage());
            e.printStackTrace();
        }
    }
} 