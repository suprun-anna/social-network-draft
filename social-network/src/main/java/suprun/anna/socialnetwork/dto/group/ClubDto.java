package suprun.anna.socialnetwork.dto.group;

public record ClubDto(
        Long id,
        String name,
        String description,
        String profilePicture,
        boolean isOpen,
        int followersCount,
        Long ownerId
) {
}
