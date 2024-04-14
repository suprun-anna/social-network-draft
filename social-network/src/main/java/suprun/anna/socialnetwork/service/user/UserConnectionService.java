package suprun.anna.socialnetwork.service.user;

import org.springframework.data.domain.Pageable;
import suprun.anna.socialnetwork.dto.userconnection.UserConnectionDto;
import suprun.anna.socialnetwork.dto.user.UserRedirectResponseDto;
import suprun.anna.socialnetwork.model.UserConnection;

import java.util.List;

public interface UserConnectionService {
    List<UserRedirectResponseDto> getAllFollowers(Long id, Pageable pageable);

    List<UserRedirectResponseDto> getAllFollowings(Long id, Pageable pageable);

    boolean isFollower(Long userId, Long followerId);

    UserConnection getConnection(Long userId, Long followerId);

    UserConnection.Connection getConnectionStatus(Long userId, Long followerId);

    UserConnectionDto followUser(Long userId, Long followerId);

    boolean unfollowUser(Long userId, Long followerId);
}
