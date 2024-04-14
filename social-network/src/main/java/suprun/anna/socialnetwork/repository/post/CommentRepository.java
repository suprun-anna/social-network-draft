package suprun.anna.socialnetwork.repository.post;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import suprun.anna.socialnetwork.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import org.springframework.data.domain.Pageable;


public interface CommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {
    @Query("SELECT c FROM Comment c "
            + "WHERE c.post.id = :postId")
    List<Comment> findAllByPostId(Long postId, Pageable pageable);
}
