package suprun.anna.socialnetwork.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@SQLDelete(sql = "UPDATE clubs SET is_deleted = TRUE WHERE id = ?")
@Where(clause = "is_deleted = FALSE")
@NoArgsConstructor
@Table(name = "clubs")
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(nullable = false)
    private String profilePicture;
    @Column(nullable = false)
    private boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinTable(name = "clubs_members",
            joinColumns = @JoinColumn(name = "club_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id"))
    private Set<User> members;

    @JoinColumn(nullable = false)
    private int memberCount;

    @OneToMany(mappedBy = "club", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Post> posts;

    @Column(nullable = false)
    private boolean isOpen;

    public Club(Long id) {
        this.id = id;
    }

    public boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(boolean open) {
        isOpen = open;
    }

    @Override
    public String toString() {
        return "Club{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                ", isDeleted=" + isDeleted +
                ", owner=" + owner.getId() +
                ", memberCount=" + memberCount +
                ", isOpen=" + isOpen +
                '}';
    }
}