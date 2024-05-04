package suprun.anna.socialnetwork.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import suprun.anna.socialnetwork.dto.post.PostDto;
import suprun.anna.socialnetwork.model.User;
import suprun.anna.socialnetwork.service.post.PostService;

import java.util.List;

@Tag(name = "Content manager", description = "Endpoints for getting content for user's home page")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/feed")
public class FeedController {
    private final PostService postService;

    @GetMapping("/get")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public List<PostDto> findAllPostsFromFollowings(Authentication authentication, Pageable pageable) {
        System.out.println("Get all posts from followings");
        User user = (User) authentication.getPrincipal();
        return postService.findAllFromFollowings(user.getId(), pageable);
    }
}
