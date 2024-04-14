package suprun.anna.socialnetwork.service.post;

import suprun.anna.socialnetwork.dto.post.like.LikeDto;
import org.springframework.data.domain.Pageable;
import suprun.anna.socialnetwork.model.User;

import java.util.List;

public interface LikeService {
    List<LikeDto> getLikes(Long postId, Pageable pageable);

    LikeDto placeLike(User user, Long postId);

    void removeLike(Long userId, Long postId);

    boolean isLiked(Long postId, Long userId);
}
