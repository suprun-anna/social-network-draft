package suprun.anna.socialnetwork.dto.group;

public record ClubUpdateDto(
        Long id,
        String name,
        String description,
        boolean isOpen
        ) {
}
