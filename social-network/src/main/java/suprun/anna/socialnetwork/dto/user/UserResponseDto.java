package suprun.anna.socialnetwork.dto.user;

public record UserResponseDto(
        Long id,
        String email,
        String username,
        String displayName,
        String bio,
        String profilePicture,
        int age
) {
}
