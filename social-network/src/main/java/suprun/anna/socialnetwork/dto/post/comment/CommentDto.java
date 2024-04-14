package suprun.anna.socialnetwork.dto.post.comment;

import java.time.LocalDateTime;

public record CommentDto(
        Long id,
        Long userId,
        Long postId,
        LocalDateTime leftAt,
        String text
) {

}
