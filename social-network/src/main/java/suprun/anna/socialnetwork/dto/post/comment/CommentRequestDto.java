package suprun.anna.socialnetwork.dto.post.comment;

public record CommentRequestDto(
        Long userId,
        Long postId,
        String text
) {
}
