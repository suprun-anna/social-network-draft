package suprun.anna.socialnetwork.service.post;


import org.springframework.data.domain.Pageable;
import suprun.anna.socialnetwork.dto.post.comment.CommentDto;
import suprun.anna.socialnetwork.dto.post.comment.CommentRequestDto;
import suprun.anna.socialnetwork.model.User;

import java.util.List;

public interface CommentService {
    CommentDto placeComment(User user, CommentRequestDto commentDto);

    CommentDto getCommentById(Long id);

    List<CommentDto> getComments(Long postId, Pageable pageable);

    void removeComment(Long userId, Long commentId);

    CommentDto editComment(Long userId, Long commentId, String text);
}
