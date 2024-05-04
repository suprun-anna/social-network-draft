package suprun.anna.socialnetwork.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import suprun.anna.socialnetwork.dto.post.PostDto;
import suprun.anna.socialnetwork.dto.post.PostUpdateDto;
import suprun.anna.socialnetwork.model.User;
import suprun.anna.socialnetwork.service.post.PostService;

import java.io.IOException;
import java.util.List;

@Tag(name = "Post manager", description = "Endpoints for post managing")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/posts")
public class PostController {
    private final PostService postService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public PostDto createNewPost(Authentication authentication,
                                 @RequestParam("title") String title,
                                 @RequestParam("content") String content,
                                 @RequestParam("picture") MultipartFile picture,
                                 @RequestParam(name = "club", required = false) Long clubId
    ) throws IOException {
        User user = (User) authentication.getPrincipal();
        System.out.println("Create post");
        return postService.save(user, title, content, picture, clubId);
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public List<PostDto> getAllPostsFromUser(@RequestParam Long id, Pageable pageable) {
        System.out.println("Get all posts by user");
        return postService.findAllByTimeOrder(id, pageable);
    }

    @GetMapping("/get")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public PostDto getPostById(@RequestParam Long id) {
        System.out.println("Get post by id");
        return postService.getById(id);
    }

    @GetMapping("/getByClub")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public List<PostDto> getPostsByClubId(@RequestParam Long id, Pageable pageable) {
        System.out.println("Get post by club id");
        return postService.findPostsByClubId(id, pageable);
    }

    @PutMapping("/edit")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public PostDto editPostById(Authentication authentication, @RequestBody PostUpdateDto postUpdateDto) {
        System.out.println("Edit post by id");
        User user = (User) authentication.getPrincipal();
        return postService.editById(user.getId(), postUpdateDto);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public void deletePostById(Authentication authentication, @RequestParam Long id) {
        System.out.println("Delete post by id");
        User user = (User) authentication.getPrincipal();
        postService.deleteById(user.getId(), id);
    }
}
