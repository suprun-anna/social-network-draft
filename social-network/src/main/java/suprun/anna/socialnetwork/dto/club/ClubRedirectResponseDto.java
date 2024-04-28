package suprun.anna.socialnetwork.dto.club;

public record ClubRedirectResponseDto(
        Long id,
        String name,
        String profilePicture,
        boolean isOpen,
        int memberCount
) {
}
