package suprun.anna.socialnetwork.dto.club;

public record ClubUpdateDto(
        Long id,
        String name,
        String description,
        boolean isOpen
        ) {
}
