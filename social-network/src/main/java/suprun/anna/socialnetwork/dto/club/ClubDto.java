package suprun.anna.socialnetwork.dto.club;

public record ClubDto(
        Long id,
        String name,
        String description,
        String profilePicture,
        boolean isOpen,
        int memberCount,
        Long ownerId
) {
}
