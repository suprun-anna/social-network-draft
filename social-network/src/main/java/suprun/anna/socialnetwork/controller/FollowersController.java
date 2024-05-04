package suprun.anna.socialnetwork.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import suprun.anna.socialnetwork.dto.user.UserResponseDto;
import suprun.anna.socialnetwork.dto.userconnection.UserConnectionDto;
import suprun.anna.socialnetwork.dto.user.UserRedirectResponseDto;
import suprun.anna.socialnetwork.model.User;
import suprun.anna.socialnetwork.model.UserConnection;
import suprun.anna.socialnetwork.service.user.UserConnectionService;
import java.util.List;
import java.util.Set;

@Tag(name = "Following process manager", description = "Endpoints for following process managing")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/follow")
public class FollowersController {
    private final UserConnectionService userConnectionService;

    @GetMapping("/getFollowers")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public List<UserRedirectResponseDto> getAllFollowers(@RequestParam Long userId, Pageable pageable){
        System.out.println("Get all followers by user");
        return userConnectionService.getAllFollowers(userId, pageable);
    }

    @GetMapping("/getFollowings")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public List<UserRedirectResponseDto> getAllFollowings(@RequestParam Long userId, Pageable pageable){
        System.out.println("Get all followings by user");
        return userConnectionService.getAllFollowings(userId, pageable);
    }

    @PutMapping("/follow")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public UserConnectionDto follow(Authentication authentication, @RequestParam Long userId){
        System.out.println("Follow");
        User user = (User) authentication.getPrincipal();
        return userConnectionService.followUser(userId, user.getId());
    }

    @PutMapping("/unfollow")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public boolean unfollow(Authentication authentication, @RequestParam Long userId){
        System.out.println("Unfollow");
        User user = (User) authentication.getPrincipal();
        return userConnectionService.unfollowUser(userId, user.getId());
    }

    @PutMapping("/forceUnfollow")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public boolean forceUnfollow(Authentication authentication, @RequestParam Long userId){
        System.out.println("Unfollow");
        User user = (User) authentication.getPrincipal();
        return userConnectionService.unfollowUser(user.getId(), userId);
    }

    @GetMapping("/checkConnection")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<UserConnection.Connection> checkConnection(Authentication authentication, @RequestParam Long userId){
        System.out.println("Check following");
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<>(userConnectionService.getConnectionStatus(user.getId(), userId), HttpStatus.OK);
    }

    @GetMapping("/recommend")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public Set<UserResponseDto> recommendUser(Authentication authentication) {
        System.out.println("recommend");
        return userConnectionService.getRandomFollowersOfFollowings((User) authentication.getPrincipal());
    }
}