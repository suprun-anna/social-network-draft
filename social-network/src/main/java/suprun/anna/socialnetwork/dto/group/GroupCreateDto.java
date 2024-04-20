package suprun.anna.socialnetwork.dto.group;

public record GroupCreateDto(
    String name,
    String description,
    String profilePicture,
    boolean isOpen,
    Long ownerId
) {
}
