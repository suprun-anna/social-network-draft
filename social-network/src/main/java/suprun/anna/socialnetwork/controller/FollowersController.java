package suprun.anna.socialnetwork.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import suprun.anna.socialnetwork.dto.userconnection.UserConnectionDto;
import suprun.anna.socialnetwork.dto.user.UserRedirectResponseDto;
import suprun.anna.socialnetwork.model.User;
import suprun.anna.socialnetwork.model.UserConnection;
import suprun.anna.socialnetwork.service.user.UserConnectionService;
import java.util.List;

@Tag(name = "Following process manager", description = "Endpoints for following process managing")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/follow")
public class FollowersController {
    private final UserConnectionService userConnectionService;

    @GetMapping("/getFollowers")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<UserRedirectResponseDto> getAllFollowers(@RequestParam Long userId, Pageable pageable){
        System.out.println("Get all followers by user");
        return userConnectionService.getAllFollowers(userId, pageable);
    }

    @GetMapping("/getFollowings")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<UserRedirectResponseDto> getAllFollowings(@RequestParam Long userId, Pageable pageable){
        System.out.println("Get all followings by user");
        return userConnectionService.getAllFollowings(userId, pageable);
    }

    @PutMapping("/follow")
    @PreAuthorize("hasRole('ROLE_USER')")
    public UserConnectionDto follow(Authentication authentication, @RequestParam Long userId){
        System.out.println("Follow");
        User user = (User) authentication.getPrincipal();
        return userConnectionService.followUser(userId, user.getId());
    }

    @PutMapping("/unfollow")
    @PreAuthorize("hasRole('ROLE_USER')")
    public boolean unfollow(Authentication authentication, @RequestParam Long userId){
        System.out.println("Unfollow");
        User user = (User) authentication.getPrincipal();
        return userConnectionService.unfollowUser(userId, user.getId());
    }

    @PutMapping("/forceUnfollow")
    @PreAuthorize("hasRole('ROLE_USER')")
    public boolean forceUnfollow(Authentication authentication, @RequestParam Long userId){
        System.out.println("Unfollow");
        User user = (User) authentication.getPrincipal();
        return userConnectionService.unfollowUser(user.getId(), userId);
    }

    @GetMapping("/checkConnection")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<UserConnection.Connection> checkConnection(Authentication authentication, @RequestParam Long userId){
        System.out.println("Check following");
        User user = (User) authentication.getPrincipal();
        return new ResponseEntity<>(userConnectionService.getConnectionStatus(user.getId(), userId), HttpStatus.OK);
    }
}