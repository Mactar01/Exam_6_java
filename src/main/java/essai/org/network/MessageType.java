package essai.org.network;

public enum MessageType {
    CONNECT,           // Connexion d'un nouveau membre
    DISCONNECT,        // Déconnexion d'un membre
    SEND_MESSAGE,      // Envoi d'un message
    RECEIVE_MESSAGE,   // Réception d'un message
    MEMBER_JOINED,     // Un nouveau membre a rejoint
    MEMBER_LEFT,       // Un membre a quitté
    MEMBER_BANNED,     // Un membre a été banni
    ERROR,             // Message d'erreur
    SUCCESS,           // Message de succès
    REQUEST_HISTORY,   // Demande d'historique des messages
    HISTORY_RESPONSE   // Réponse avec l'historique
} 