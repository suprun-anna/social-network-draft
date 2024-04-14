package suprun.anna.socialnetwork.repository.post;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import suprun.anna.socialnetwork.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long>, JpaSpecificationExecutor<Like> {
    @Query("SELECT l FROM Like l "
            + "WHERE l.post.id = :postId AND l.isDeleted = FALSE "
            + "ORDER BY l.leftAt DESC"
    )
    List<Like> findAllByTimeOrder(Long postId, Pageable pageable);

    @Query("SELECT l FROM Like l " +
            "WHERE l.user.id = :userId AND l.post.id = :postId " +
            "AND (l.isDeleted = FALSE OR l.isDeleted = TRUE)")
    Optional<Like> getLike(Long userId, Long postId);
}

