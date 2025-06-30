package essai.org.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.application.Platform;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import essai.org.network.ClientNetwork;
import essai.org.model.ChatMessage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

public class ClientController {
    @FXML
    private TextField pseudoField;
    @FXML
    private Button connectButton;
    @FXML
    private ListView<ChatMessage> messageList;
    @FXML
    private TextField messageField;
    @FXML
    private Button sendButton;
    @FXML
    private Label statusLabel;
    @FXML
    private Button disconnectButton;
    @FXML
    private Label memberCount;

    private ObservableList<ChatMessage> messages = FXCollections.observableArrayList();
    private ClientNetwork clientNetwork;
    private int connectedMembers = 0;

    @FXML
    public void initialize() {
        messageList.setItems(messages);
        messageList.setCellFactory(param -> new ListCell<ChatMessage>() {
            @Override
            protected void updateItem(ChatMessage item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("");
                } else {
                    // Créer un conteneur pour le message avec style approprié
                    VBox messageContainer = new VBox(5);
                    messageContainer.setMaxWidth(300);
                    
                    // Label pour le nom de l'expéditeur
                    Label senderLabel = new Label(item.getSender());
                    senderLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12px; -fx-text-fill: #128C7E;");
                    
                    // Label pour le contenu du message
                    Label contentLabel = new Label(item.getContent());
                    contentLabel.setWrapText(true);
                    contentLabel.setMaxWidth(280);
                    
                    // Appliquer le style selon le type de message
                    String styleClass = getMessageStyleClass(item);
                    contentLabel.getStyleClass().add(styleClass);
                    
                    // Aligner à droite pour les messages de l'utilisateur
                    if (item.getType() == ChatMessage.MessageType.USER_MESSAGE && 
                        item.getSender().equals("Moi")) {
                        messageContainer.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
                        senderLabel.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
                    } else {
                        messageContainer.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                        senderLabel.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                    }
                    
                    messageContainer.getChildren().addAll(senderLabel, contentLabel);
                    setGraphic(messageContainer);
                    setText(null);
                    
                    // Animation d'apparition
                    animateMessage(messageContainer);
                }
            }
        });
        
        // Initialiser l'état des contrôles
        sendButton.setDisable(true);
        messageField.setDisable(true);
        disconnectButton.setDisable(true);
        messageField.setOnKeyPressed(this::onMessageFieldKeyPressed);
        
        // Mettre à jour le statut initial
        updateStatus("Prêt à se connecter", "ready");
        updateMemberCount(0);
    }

    private String getMessageStyleClass(ChatMessage message) {
        switch (message.getType()) {
            case USER_MESSAGE:
                return message.getSender().equals("Moi") ? "message-user" : "message-other";
            case SYSTEM_MESSAGE:
                return "message-system";
            case JOIN_MESSAGE:
                return "message-join";
            case LEAVE_MESSAGE:
                return "message-leave";
            case BAN_MESSAGE:
                return "message-ban";
            default:
                return "message-system";
        }
    }

    private void animateMessage(Node messageNode) {
        // Animation de fade-in
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), messageNode);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        
        // Animation de slide-in
        TranslateTransition slideIn = new TranslateTransition(Duration.millis(300), messageNode);
        slideIn.setFromX(-20);
        slideIn.setToX(0);
        
        fadeIn.play();
        slideIn.play();
    }

    @FXML
    private void onConnect() {
        updateStatus("Connexion en cours...", "connecting");
        clientNetwork = new ClientNetwork(this);
        String pseudo = pseudoField.getText().trim();
        
        if (pseudo.isEmpty()) {
            showError("Veuillez saisir un pseudo.");
            return;
        }
        
        if (pseudo.length() < 2) {
            showError("Le pseudo doit contenir au moins 2 caractères.");
            return;
        }
        
        clientNetwork.connect(pseudo, "localhost", 8080);
        sendButton.setDisable(false);
        messageField.setDisable(false);
        pseudoField.setDisable(true);
        connectButton.setDisable(true);
        disconnectButton.setDisable(false);
        
        // Animation de connexion
        animateConnection();
    }

    private void animateConnection() {
        // Animation du bouton de connexion
        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), connectButton);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.3);
        fadeOut.play();
        
        // Animation du bouton de déconnexion
        FadeTransition fadeIn = new FadeTransition(Duration.millis(200), disconnectButton);
        fadeIn.setFromValue(0.3);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    @FXML
    private void onSend() {
        String msg = messageField.getText().trim();
        if (!msg.isEmpty()) {
            if (clientNetwork != null) {
                clientNetwork.sendMessage(getPseudo(), msg);
            }
            messages.add(ChatMessage.createUserMessage("Moi", msg));
            messageField.clear();
            
            // Faire défiler vers le bas
            Platform.runLater(() -> {
                messageList.scrollTo(messages.size() - 1);
            });
        }
    }

    private void onMessageFieldKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            onSend();
        }
    }

    public void addMessage(ChatMessage msg) {
        Platform.runLater(() -> {
            messages.add(msg);
            
            // Faire défiler vers le bas
            messageList.scrollTo(messages.size() - 1);
            
            // Jouer une notification pour les messages des autres
            if (msg.getType() == ChatMessage.MessageType.USER_MESSAGE && 
                !msg.getSender().equals("Moi")) {
                playNotificationSound();
                showNotification("Nouveau message de " + msg.getSender());
            }
            
            // Mettre à jour le compteur de membres pour les messages de connexion/déconnexion
            if (msg.getType() == ChatMessage.MessageType.JOIN_MESSAGE) {
                connectedMembers++;
                updateMemberCount(connectedMembers);
            } else if (msg.getType() == ChatMessage.MessageType.LEAVE_MESSAGE) {
                connectedMembers = Math.max(0, connectedMembers - 1);
                updateMemberCount(connectedMembers);
            }
        });
    }

    public void addMessage(String content) {
        Platform.runLater(() -> {
            messages.add(ChatMessage.createSystemMessage(content));
            messageList.scrollTo(messages.size() - 1);
        });
    }

    public void showError(String error) {
        Platform.runLater(() -> updateStatus(error, "error"));
    }

    public void showSuccess(String message) {
        Platform.runLater(() -> updateStatus(message, "success"));
    }

    private void updateStatus(String message, String type) {
        statusLabel.setText(message);
        
        // Appliquer le style selon le type
        statusLabel.getStyleClass().clear();
        statusLabel.getStyleClass().add("status-text");
        
        switch (type) {
            case "error":
                statusLabel.setStyle("-fx-text-fill: #FF4444; -fx-font-weight: bold;");
                break;
            case "success":
                statusLabel.setStyle("-fx-text-fill: #25D366; -fx-font-weight: bold;");
                break;
            case "connecting":
                statusLabel.setStyle("-fx-text-fill: #FF9500; -fx-font-weight: bold;");
                break;
            default:
                statusLabel.setStyle("-fx-text-fill: #DCF8C6;");
        }
    }

    private void updateMemberCount(int count) {
        memberCount.setText(count + " membre" + (count > 1 ? "s" : ""));
    }

    private void showNotification(String message) {
        // Animation de notification
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: #25D366; -fx-font-weight: bold;");
        
        // Faire clignoter brièvement
        FadeTransition blink = new FadeTransition(Duration.millis(500), statusLabel);
        blink.setFromValue(1.0);
        blink.setToValue(0.3);
        blink.setCycleCount(2);
        blink.setAutoReverse(true);
        blink.play();
    }

    public String getPseudo() {
        return pseudoField.getText();
    }

    public void disableInput() {
        sendButton.setDisable(true);
        messageField.setDisable(true);
        pseudoField.setDisable(false);
        connectButton.setDisable(false);
        disconnectButton.setDisable(true);
        connectedMembers = 0;
        updateMemberCount(0);
    }

    @FXML
    public void onDisconnect() {
        if (clientNetwork != null) {
            clientNetwork.disconnect(getPseudo());
        }
        disableInput();
        updateStatus("Déconnecté.", "disconnected");
        
        // Animation de déconnexion
        animateDisconnection();
    }

    private void animateDisconnection() {
        // Animation du bouton de déconnexion
        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), disconnectButton);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.3);
        fadeOut.play();
        
        // Animation du bouton de connexion
        FadeTransition fadeIn = new FadeTransition(Duration.millis(200), connectButton);
        fadeIn.setFromValue(0.3);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    private void playNotificationSound() {
        // Pour l'instant, on affiche juste une notification visuelle
        // TODO: Ajouter un vrai fichier son .wav dans src/main/resources/sounds/
        Platform.runLater(() -> {
            showNotification("🔔 Nouveau message reçu !");
        });
    }
} 