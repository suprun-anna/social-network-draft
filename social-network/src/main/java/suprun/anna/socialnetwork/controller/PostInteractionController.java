package suprun.anna.socialnetwork.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import suprun.anna.socialnetwork.dto.post.comment.CommentDto;
import suprun.anna.socialnetwork.dto.post.comment.CommentRequestDto;
import suprun.anna.socialnetwork.dto.post.like.LikeDto;
import suprun.anna.socialnetwork.model.User;
import suprun.anna.socialnetwork.service.post.CommentService;
import suprun.anna.socialnetwork.service.post.LikeService;

import java.util.List;

@Tag(name = "Post interactions manager", description = "Endpoints for managing likes and comments")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/interact")
public class PostInteractionController {
    private final LikeService likeService;
    private final CommentService commentService;

    @GetMapping("/likes/getAll")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<LikeDto> getLikes(@RequestParam Long postId, Pageable pageable) {
        System.out.println("Get all likes");
        return likeService.getLikes(postId, pageable);
    }

    @GetMapping("/likes/isLiked")
    @PreAuthorize("hasRole('ROLE_USER')")
    public boolean getLikes(@RequestParam Long postId, @RequestParam Long userId) {
        boolean isLiked = likeService.isLiked(postId, userId);
        System.out.println("Is liked? " + isLiked);
        return isLiked;
    }

    @PostMapping("/likes/place")
    @PreAuthorize("hasRole('ROLE_USER')")
    public LikeDto placeLike(Authentication authentication, @RequestParam Long postId) {
        System.out.println("Place like");
        User user = (User) authentication.getPrincipal();
        return likeService.placeLike(user, postId);
    }

    @DeleteMapping("/likes/remove")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void removeLike(Authentication authentication, @RequestParam Long postId) {
        System.out.println("Remove like");
        User user = (User) authentication.getPrincipal();
        likeService.removeLike(user.getId(), postId);
    }

    @GetMapping("/comments/getAll")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<CommentDto> getAllComments(@RequestParam Long postId, Pageable pageable) {
        System.out.println("Get all comments");
        return commentService.getComments(postId, pageable);
    }

    @GetMapping("/comments/get")
    @PreAuthorize("hasRole('ROLE_USER')")
    public CommentDto getComment(@RequestParam Long commentId) {
        System.out.println("Get comment");
        return commentService.getCommentById(commentId);
    }

    @PostMapping("/comments/place")
    @PreAuthorize("hasRole('ROLE_USER')")
    public CommentDto placeComment(Authentication authentication, @RequestBody CommentRequestDto commentDto) {
        System.out.println("Place comment");
        User user = (User) authentication.getPrincipal();
        return commentService.placeComment(user, commentDto);
    }

    @DeleteMapping("/comments/remove")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void removeComment(Authentication authentication, @RequestParam Long commentId) {
        System.out.println("Remove comment");
        User user = (User) authentication.getPrincipal();
        commentService.removeComment(user.getId(), commentId);
    }

    @PutMapping("/comments/edit")
    @PreAuthorize("hasRole('ROLE_USER')")
    public CommentDto editComment(Authentication authentication, @RequestParam Long commentId, @RequestParam String text) {
        System.out.println("Edit comment");
        User user = (User) authentication.getPrincipal();
        return commentService.editComment(user.getId(), commentId, text);
    }
}
