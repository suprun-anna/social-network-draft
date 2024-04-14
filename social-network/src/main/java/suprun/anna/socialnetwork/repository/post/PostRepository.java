package suprun.anna.socialnetwork.repository.post;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import suprun.anna.socialnetwork.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
    @Query("SELECT p FROM Post p "
            + "WHERE p.user.id = :id "
            + "ORDER BY p.createdAt DESC"
            )
    List<Post> findAllByTimeOrder(Long id, Pageable pageable);

    @Query("SELECT p FROM Post p "
            + "LEFT JOIN UserConnection uc ON p.user.id = uc.user.id "
            + "WHERE uc.follower.id = :userId AND uc.isDeleted = FALSE "
            + "ORDER BY p.createdAt DESC"
    )
    List<Post> findAllFromFollowingsByTimeOrder(Long userId, Pageable pageable);

    @Query("SELECT p FROM Post p "
            + "LEFT JOIN UserConnection uc ON p.user.id = uc.user.id "
            + "WHERE uc.follower.id = :userId AND uc.isDeleted = FALSE "
            + "ORDER BY p.likeCount DESC"
    )
    List<Post> findAllFromFollowingsByActivity(Long userId, Pageable pageable);
}
