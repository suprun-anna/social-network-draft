package suprun.anna.socialnetwork.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import suprun.anna.socialnetwork.dto.user.UserRedirectResponseDto;
import suprun.anna.socialnetwork.service.user.UserConnectionService;
import suprun.anna.socialnetwork.service.user.UserService;

import java.util.List;

@Tag(name = "Following process manager", description = "Endpoints for following process managing")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/search")
public class SearchController {
    private final UserService userService;

    @GetMapping("/user")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<UserRedirectResponseDto> getAllFollowers(@RequestParam String username, Pageable pageable){
        System.out.println("search for user");
        return userService.findByPartialUsername(username, pageable);
    }
}