package suprun.anna.socialnetwork.dto.post.like;

import java.time.LocalDateTime;

public record LikeDto(
        Long id,
        Long userId,
        Long postId,
        LocalDateTime leftAt
) {
}
