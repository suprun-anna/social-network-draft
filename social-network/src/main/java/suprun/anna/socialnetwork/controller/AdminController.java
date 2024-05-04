package suprun.anna.socialnetwork.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import suprun.anna.socialnetwork.dto.user.UserInfoResponseDto;
import suprun.anna.socialnetwork.model.User;
import suprun.anna.socialnetwork.service.user.AdminService;
import suprun.anna.socialnetwork.service.user.UserService;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/admin")
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/ban")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void banUser(@RequestParam Long userId, @RequestParam String reason) {
        adminService.banUser(userId, reason);
    }

    @PostMapping("/unban")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void unbanUser(@RequestParam Long userId) {
        adminService.unbanUser(userId);
    }

    @GetMapping("/userStatus")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public boolean banUser(@RequestParam Long userId) {
        return adminService.checkIfBannedById(userId);
    }
}
