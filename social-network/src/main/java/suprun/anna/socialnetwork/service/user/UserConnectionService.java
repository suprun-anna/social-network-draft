package suprun.anna.socialnetwork.service.user;

import org.springframework.data.domain.Pageable;
import suprun.anna.socialnetwork.dto.user.UserResponseDto;
import suprun.anna.socialnetwork.dto.userconnection.UserConnectionDto;
import suprun.anna.socialnetwork.dto.user.UserRedirectResponseDto;
import suprun.anna.socialnetwork.model.User;
import suprun.anna.socialnetwork.model.UserConnection;

import java.util.List;
import java.util.Set;

public interface UserConnectionService {
    List<UserRedirectResponseDto> getAllFollowers(Long id, Pageable pageable);

    Set<UserResponseDto> getRandomFollowersOfFollowings(User user);

    List<UserRedirectResponseDto> getAllFollowings(Long id, Pageable pageable);

    boolean isFollower(Long userId, Long followerId);

    UserConnection getConnection(Long userId, Long followerId);

    UserConnection.Connection getConnectionStatus(Long userId, Long followerId);

    UserConnectionDto followUser(Long userId, Long followerId);

    boolean unfollowUser(Long userId, Long followerId);
}
