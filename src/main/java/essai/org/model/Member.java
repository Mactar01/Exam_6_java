package essai.org.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "members")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 50)
    private String pseudo;
    
    @Column(name = "join_date", nullable = false)
    private LocalDateTime joinDate;
    
    @Column(name = "is_online", nullable = false)
    private boolean isOnline;
    
    @Column(name = "is_banned", nullable = false)
    private boolean isBanned;
    
    @Column(name = "ban_reason")
    private String banReason;
    
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Message> messages = new ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
        joinDate = LocalDateTime.now();
        isOnline = true;
        isBanned = false;
    }
} 