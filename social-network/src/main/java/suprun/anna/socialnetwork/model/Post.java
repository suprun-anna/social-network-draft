package suprun.anna.socialnetwork.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@SQLDelete(sql = "UPDATE posts SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
@Table(name = "posts")
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private String pictureUrl;
    private int likeCount;
    private int commentCount;
    @Column(nullable = false)
    private boolean isUpdated;
    @Column(nullable = false)
    private boolean isDeleted;

    public void setIsUpdated(boolean updated) {
        isUpdated = updated;
    }

    public boolean getIsUpdated() {
        return isUpdated;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private Set<Like> likes;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private Set<Comment> comments;

    public Post(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", likeCount=" + likeCount +
                ", commentCount=" + commentCount +
                ", isDeleted=" + isDeleted +
                ", userId=" + user.getId() +
                ", likes=" + likes +
                ", comments=" + comments +
                '}';
    }
}
