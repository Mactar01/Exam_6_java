package essai.org;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.paint.Color;

public class WhatsAppClient extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/ClientView.fxml"));
        
        // Créer la scène avec le CSS
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/view/styles.css").toExternalForm());
        
        // Configurer la fenêtre
        primaryStage.setTitle("💬 WhatsApp L3IAGE - Client");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setMinWidth(500);
        primaryStage.setMinHeight(600);
        
        // Centrer la fenêtre
        primaryStage.centerOnScreen();
        
        // Afficher la fenêtre
        primaryStage.show();
        
        // Focus sur le champ pseudo
        primaryStage.setOnShown(e -> {
            try {
                javafx.scene.control.TextField pseudoField = (javafx.scene.control.TextField) root.lookup("#pseudoField");
                if (pseudoField != null) {
                    pseudoField.requestFocus();
                }
            } catch (Exception ex) {
                // Ignorer les erreurs de focus
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
} 