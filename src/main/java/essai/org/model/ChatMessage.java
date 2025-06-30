package essai.org.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {
    private String sender;
    private String content;
    private LocalDateTime timestamp;
    private MessageType type;
    
    public enum MessageType {
        USER_MESSAGE,
        SYSTEM_MESSAGE,
        JOIN_MESSAGE,
        LEAVE_MESSAGE,
        BAN_MESSAGE
    }
    
    public String getFormattedMessage() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String time = timestamp != null ? timestamp.format(formatter) : "";
        
        switch (type) {
            case USER_MESSAGE:
                return String.format("[%s] %s: %s", time, sender, content);
            case SYSTEM_MESSAGE:
                return String.format("[%s] %s", time, content);
            case JOIN_MESSAGE:
                return String.format("[%s] 🟢 %s a rejoint le groupe", time, sender);
            case LEAVE_MESSAGE:
                return String.format("[%s] 🔴 %s a quitté le groupe", time, sender);
            case BAN_MESSAGE:
                return String.format("[%s] ⚠️ %s a été banni: %s", time, sender, content);
            default:
                return String.format("[%s] %s", time, content);
        }
    }
    
    public static ChatMessage createUserMessage(String sender, String content) {
        return ChatMessage.builder()
                .sender(sender)
                .content(content)
                .timestamp(LocalDateTime.now())
                .type(MessageType.USER_MESSAGE)
                .build();
    }
    
    public static ChatMessage createSystemMessage(String content) {
        return ChatMessage.builder()
                .content(content)
                .timestamp(LocalDateTime.now())
                .type(MessageType.SYSTEM_MESSAGE)
                .build();
    }
    
    public static ChatMessage createJoinMessage(String sender) {
        return ChatMessage.builder()
                .sender(sender)
                .timestamp(LocalDateTime.now())
                .type(MessageType.JOIN_MESSAGE)
                .build();
    }
    
    public static ChatMessage createLeaveMessage(String sender) {
        return ChatMessage.builder()
                .sender(sender)
                .timestamp(LocalDateTime.now())
                .type(MessageType.LEAVE_MESSAGE)
                .build();
    }
    
    public static ChatMessage createBanMessage(String sender, String reason) {
        return ChatMessage.builder()
                .sender(sender)
                .content(reason)
                .timestamp(LocalDateTime.now())
                .type(MessageType.BAN_MESSAGE)
                .build();
    }
} 