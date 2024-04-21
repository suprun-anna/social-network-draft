package suprun.anna.socialnetwork.dto.group;

public record ClubCreateDto(
        String name,
        String description,
        boolean isOpen
) {
}
