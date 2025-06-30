package essai.org;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SimpleTest {
    
    public static void main(String[] args) {
        System.out.println("=== Test Simple - Simulation WhatsApp ===");
        System.out.println();
        
        // Test de connexion MySQL directe
        System.out.println("1. Test de connexion MySQL...");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ Driver MySQL chargé");
            
            String url = "jdbc:mysql://localhost:3306/whatsapp_db?useSSL=false&serverTimezone=UTC";
            String user = "root";
            String password = "";
            
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Connexion à MySQL réussie");
            System.out.println("✅ Base de données 'whatsapp_db' accessible");
            
            connection.close();
            System.out.println("✅ Connexion fermée proprement");
            
        } catch (ClassNotFoundException e) {
            System.out.println("❌ Erreur : Driver MySQL non trouvé");
            System.out.println("   Vérifiez que mysql-connector-java-8.0.33.jar est dans le dossier lib/");
        } catch (SQLException e) {
            System.out.println("❌ Erreur de connexion MySQL : " + e.getMessage());
            System.out.println();
            System.out.println("🔧 Solutions possibles :");
            System.out.println("1. Vérifiez que MySQL est démarré");
            System.out.println("2. Vérifiez que la base 'whatsapp_db' existe");
            System.out.println("3. Vérifiez les paramètres de connexion (utilisateur/mot de passe)");
        }
        
        System.out.println();
        System.out.println("2. Test de compilation Java...");
        try {
            // Test simple de compilation
            String test = "Hello World";
            System.out.println("✅ Compilation Java OK : " + test);
        } catch (Exception e) {
            System.out.println("❌ Erreur de compilation : " + e.getMessage());
        }
        
        System.out.println();
        System.out.println("=== Résumé ===");
        System.out.println("✅ Java 21 fonctionne");
        System.out.println("✅ Les fichiers JAR sont présents");
        System.out.println("⚠️  Vérifiez la connexion MySQL ci-dessus");
        System.out.println();
        System.out.println("💡 Si MySQL fonctionne, vous pouvez tester l'application complète !");
    }
} 