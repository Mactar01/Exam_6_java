package essai.org.network;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NetworkMessage {
    
    private MessageType type;
    private String sender;
    private String content;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    
    private String errorMessage;
    private Object data;
    
    public static NetworkMessage createConnect(String pseudo) {
        return NetworkMessage.builder()
                .type(MessageType.CONNECT)
                .sender(pseudo)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    public static NetworkMessage createDisconnect(String pseudo) {
        return NetworkMessage.builder()
                .type(MessageType.DISCONNECT)
                .sender(pseudo)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    public static NetworkMessage createSendMessage(String sender, String content) {
        return NetworkMessage.builder()
                .type(MessageType.SEND_MESSAGE)
                .sender(sender)
                .content(content)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    public static NetworkMessage createReceiveMessage(String sender, String content) {
        return NetworkMessage.builder()
                .type(MessageType.RECEIVE_MESSAGE)
                .sender(sender)
                .content(content)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    public static NetworkMessage createError(String errorMessage) {
        return NetworkMessage.builder()
                .type(MessageType.ERROR)
                .errorMessage(errorMessage)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    public static NetworkMessage createSuccess(String content) {
        return NetworkMessage.builder()
                .type(MessageType.SUCCESS)
                .content(content)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    public static NetworkMessage createMemberJoined(String pseudo) {
        return NetworkMessage.builder()
                .type(MessageType.MEMBER_JOINED)
                .sender(pseudo)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    public static NetworkMessage createMemberLeft(String pseudo) {
        return NetworkMessage.builder()
                .type(MessageType.MEMBER_LEFT)
                .sender(pseudo)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    public static NetworkMessage createMemberBanned(String pseudo, String reason) {
        return NetworkMessage.builder()
                .type(MessageType.MEMBER_BANNED)
                .sender(pseudo)
                .content(reason)
                .timestamp(LocalDateTime.now())
                .build();
    }
} 