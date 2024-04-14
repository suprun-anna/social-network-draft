package suprun.anna.socialnetwork.service.post.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import suprun.anna.socialnetwork.dto.post.like.LikeDto;
import suprun.anna.socialnetwork.mapper.LikeMapper;
import suprun.anna.socialnetwork.model.Like;
import suprun.anna.socialnetwork.model.Post;
import suprun.anna.socialnetwork.model.User;
import suprun.anna.socialnetwork.repository.post.LikeRepository;
import suprun.anna.socialnetwork.repository.post.PostRepository;
import suprun.anna.socialnetwork.repository.user.UserRepository;
import suprun.anna.socialnetwork.service.post.LikeService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final LikeMapper likeMapper;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Override
    public List<LikeDto> getLikes(Long postId, Pageable pageable) {
        return likeRepository.findAllByTimeOrder(postId, pageable).stream()
                .filter(like -> !like.isDeleted())
                .map(likeMapper::toDto)
                .toList();
    }

    @Override
    public LikeDto placeLike(User user, Long postId) {
        Optional<Like> likeOptional = likeRepository.getLike(user.getId(), postId);
        Like like;
        if (likeOptional.isEmpty() || likeOptional.get().isDeleted()) {
            Post post = postRepository.findById(postId).orElseThrow(
                    () -> new NoSuchElementException("No post with id=" + postId));
            post.setLikeCount(post.getLikeCount() + 1);
            postRepository.save(post);
            if (likeOptional.isEmpty()) {
                like = new Like();
                like.setUser(user);
                like.setPost(post);
            } else {
                like = likeOptional.get();
                like.setDeleted(false);
            }
            like.setLeftAt(LocalDateTime.now());
            return likeMapper.toDto(likeRepository.save(like));
        } else throw new RuntimeException("Like is already present");
    }

    @Override
    public void removeLike(Long userId, Long postId) {
        Like like = likeRepository.getLike(userId, postId).orElseThrow(
                () -> new NoSuchElementException("No like from userId=" + userId + " on postId=" + postId));
        if (like.isDeleted()) return;
        like.setDeleted(true);
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new NoSuchElementException("No post with id=" + postId));
        post.setLikeCount(post.getLikeCount() - 1);
        postRepository.save(post);
        likeRepository.save(like);
    }

    @Override
    public boolean isLiked(Long postId, Long userId) {
        Optional<Like> likeOptional = likeRepository.getLike(userId, postId);
        if (likeOptional.isEmpty()) return false;
        return !likeOptional.get().isDeleted();
    }
}
