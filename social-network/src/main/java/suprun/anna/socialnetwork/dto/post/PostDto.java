package suprun.anna.socialnetwork.dto.post;

import java.time.LocalDateTime;

public record PostDto(
        Long id,
        String title,
        String content,
        Long userId,
        Long clubId,
        LocalDateTime createdAt,
        String pictureUrl,
        int likeCount,
        int commentCount,
        boolean isUpdated
) {
}
