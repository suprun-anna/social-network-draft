package suprun.anna.socialnetwork.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import suprun.anna.socialnetwork.dto.club.ClubRedirectResponseDto;
import suprun.anna.socialnetwork.dto.user.UserRedirectResponseDto;
import suprun.anna.socialnetwork.service.club.ClubService;
import suprun.anna.socialnetwork.service.user.UserConnectionService;
import suprun.anna.socialnetwork.service.user.UserService;

import java.util.List;

@Tag(name = "Following process manager", description = "Endpoints for following process managing")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/search")
public class SearchController {
    private final UserService userService;
    private final ClubService clubService;

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public List<UserRedirectResponseDto> searchUsers(@RequestParam String username, Pageable pageable){
        System.out.println("search for user");
        return userService.findByPartialUsername(username, pageable);
    }

    @GetMapping("/club")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public List<ClubRedirectResponseDto> searchClubs(@RequestParam String name, Pageable pageable){
        System.out.println("search for clubs");
        return clubService.findOpenClubsByPartialName(name, pageable);
    }
}