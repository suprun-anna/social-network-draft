package suprun.anna.socialnetwork.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import suprun.anna.socialnetwork.dto.user.UserInfoResponseDto;
import suprun.anna.socialnetwork.dto.user.UserResponseDto;
import suprun.anna.socialnetwork.model.User;
import suprun.anna.socialnetwork.service.user.UserService;

import java.io.IOException;

@Tag(name = "Profile manager", description = "Endpoints for profile data managing")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/user")
public class ProfileController {
    private final UserService userService;

    @Operation(summary = "Get my info", description = "Get my info")
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public UserInfoResponseDto getMyInfo(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return userService.getUserInfo(user);
    }

    @Operation(summary = "Get user info", description = "Get user info")
    @GetMapping("/id")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public UserInfoResponseDto getUserInfoById(@RequestParam Long userId) {
        System.out.println("Get user info");
        return userService.getUserInfo(userService.getById(userId));
    }

    @Operation(summary = "Get user info", description = "Get user info")
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public UserInfoResponseDto getUserInfoByUsername(@RequestParam String username) {
        System.out.println("Get user info");
        return userService.getUserInfo(userService.getByUsername(username));
    }

    @Operation(summary = "Update profile picture", description = "Update profile picture")
    @PostMapping("/update/pfp")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public UserResponseDto updatePfp(Authentication authentication,
                                     @RequestParam MultipartFile profilePicture) throws IOException {
        User user = (User) authentication.getPrincipal();
        return userService.updateProfilePicture(user, profilePicture);
    }

    @Operation(summary = "Update bio", description = "Update bio")
    @PostMapping("/update/bio")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public UserResponseDto updateBio(Authentication authentication,
                                     @RequestParam String bio) {
        User user = (User) authentication.getPrincipal();
        return userService.updateBio(user, bio);
    }

    @Operation(summary = "Update display name", description = "Update display name")
    @PostMapping("/update/displayName")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public UserResponseDto updateDisplayName(Authentication authentication,
                                     @RequestParam String displayName) {
        User user = (User) authentication.getPrincipal();
        return userService.updateDisplayName(user, displayName);
    }

    @Operation(summary = "Update age", description = "Update age")
    @PostMapping("/update/age")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public UserResponseDto updateDisplayName(Authentication authentication,
                                             @RequestParam int age) {
        User user = (User) authentication.getPrincipal();
        return userService.updateAge(user, age);
    }
}
