package suprun.anna.socialnetwork.dto.post;

public record PostUpdateDto (
        Long id,
        Long clubId,
        String title,
        String content
) {
}
