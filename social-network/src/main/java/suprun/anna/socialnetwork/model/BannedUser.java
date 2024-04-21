package suprun.anna.socialnetwork.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "banned_users")
@RequiredArgsConstructor
@NoArgsConstructor
public class BannedUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "ban_reason")
    private String banReason;

    @Column(name = "banned_at", nullable = false)
    private LocalDateTime bannedAt;
}
