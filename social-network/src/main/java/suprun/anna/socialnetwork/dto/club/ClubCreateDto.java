package suprun.anna.socialnetwork.dto.club;

public record ClubCreateDto(
        String name,
        String description,
        boolean isOpen
) {
}
