package suprun.anna.socialnetwork.service.post.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import suprun.anna.socialnetwork.dto.post.comment.CommentDto;
import suprun.anna.socialnetwork.dto.post.comment.CommentRequestDto;
import suprun.anna.socialnetwork.mapper.CommentMapper;
import suprun.anna.socialnetwork.model.Comment;
import suprun.anna.socialnetwork.model.Post;
import suprun.anna.socialnetwork.model.User;
import suprun.anna.socialnetwork.repository.CommentRepository;
import suprun.anna.socialnetwork.repository.PostRepository;
import suprun.anna.socialnetwork.repository.UserRepository;
import suprun.anna.socialnetwork.service.post.CommentService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Override
    public CommentDto placeComment(User user, CommentRequestDto commentDto) {
        String text = commentDto.text().trim();
        if (text.isBlank() || text.length() == 0) {
            throw new RuntimeException("Error with comment text");
        } else {
            Comment comment = commentMapper.toModel(commentDto);
            comment.setUser(user);
            Post post = postRepository.findById(commentDto.postId()).orElseThrow(NoSuchElementException::new);
            post.setCommentCount(post.getCommentCount() +1);
            comment.setPost(post);
            comment.setText(text);
            comment.setLeftAt(LocalDateTime.now());
            return commentMapper.toDto(commentRepository.save(comment));
        }
    }

    @Override
    public CommentDto getCommentById(Long id) {
        return commentMapper.toDto(commentRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("Can't find comment with id=" + id)));
    }


    @Override
    public List<CommentDto> getComments(Long postId, Pageable pageable) {
        return commentRepository.findAllByPostId(postId, pageable).stream()
                .map(commentMapper::toDto)
                .toList();
    }

    @Override
    public void removeComment(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(NoSuchElementException::new);
        Post post = postRepository.findById(comment.getPost().getId()).orElseThrow(NoSuchElementException::new);
        post.setCommentCount(post.getCommentCount() -1);
        if (userRepository.findById(userId).isPresent() && comment.getUser().getId().equals(userId)) {
            commentRepository.delete(comment);
        }
    }

    @Override
    public CommentDto editComment(Long userId, Long commentId, String text) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NoSuchElementException("No comment with id="+commentId)
        );
        if (!userId.equals(comment.getUser().getId())){
            throw new RuntimeException("Error with editing comment. User id error");
        }
        if (text.isBlank() || text.length() == 0) {
            throw new RuntimeException("Error with editing comment");
        } else {
            comment.setText(text);
            return commentMapper.toDto(commentRepository.save(comment));
        }
    }
}
