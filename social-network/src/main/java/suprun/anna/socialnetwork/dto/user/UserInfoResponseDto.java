package suprun.anna.socialnetwork.dto.user;

public record UserInfoResponseDto (
        Long id,
        String username,
        String displayName,
        String bio,
        String profilePicture,
        int followerCount,
        int followingCount,
        int age
){
}
