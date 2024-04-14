package suprun.anna.socialnetwork.service.post.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import suprun.anna.socialnetwork.dto.post.PostDto;
import suprun.anna.socialnetwork.dto.post.PostUpdateDto;
import suprun.anna.socialnetwork.mapper.PostMapper;
import suprun.anna.socialnetwork.model.Post;
import suprun.anna.socialnetwork.model.User;
import suprun.anna.socialnetwork.repository.post.PostRepository;
import suprun.anna.socialnetwork.service.post.FileUploadService;
import suprun.anna.socialnetwork.service.post.PostService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final FileUploadService fileUploadService;
    private final PostMapper postMapper;

    @Override
    public PostDto save(User user, String title, String content, MultipartFile picture) throws IOException {
        if (picture.isEmpty()) throw new RuntimeException("Can't create post! Picture is empty!");
        Post post = new Post();
        post.setUser(user);
        post.setTitle(title);
        post.setContent(content);
        post.setCreatedAt(LocalDateTime.now());
        String filename = fileUploadService.saveFile(picture);
        post.setPictureUrl(filename);
        try {
            post = postRepository.save(post);
        } catch (Exception e) {
            fileUploadService.deleteFile(filename);
            throw new RuntimeException("Can't create post!");
        }
        return postMapper.toDto(post);
    }

    @Override
    public List<PostDto> findAll() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::toDto)
                .toList();
    }

    @Override
    public List<PostDto> findAll(User user, Pageable pageable) {
        return postRepository.findAll(pageable)
                .stream()
                .map(postMapper::toDto)
                .toList();
    }

    @Override
    public List<PostDto> findAllByTimeOrder(Long id, Pageable pageable) {
        return postRepository.findAllByTimeOrder(id, pageable)
                .stream()
                .map(postMapper::toDto)
                .toList();
    }

    @Override
    public PostDto getById(Long id) {
        return postMapper.toDto(postRepository.findById(id).orElseThrow(()
                -> new NoSuchElementException("No post with id=" + id)));
    }

    @Override
    public PostDto editById(Long userId, PostUpdateDto postUpdateDto) {
        Post post = postRepository.findById(postUpdateDto.id()).orElseThrow(NoSuchElementException::new);
        if (!post.getUser().getId().equals(userId)) {
            throw new RuntimeException("Post postId=" + post.getId() + " doesn't belong to user userId=" + userId);
        }
        post.setContent(postUpdateDto.content());
        post.setTitle(postUpdateDto.title());
        post.setIsUpdated(true);
        return postMapper.toDto(postRepository.save(post));
    }

    @Override
    public List<PostDto> findAllFromFollowings(Long userId, Pageable pageable) {
        return postRepository.findAllFromFollowingsByTimeOrder(userId, pageable)
                .stream()
                .map(postMapper::toDto)
                .toList();
    }

    @Override
    public void deleteById(Long userId, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(NoSuchElementException::new);
        if (!post.getUser().getId().equals(userId)) {
            throw new RuntimeException("Post postId=" + post.getId() + " doesn't belong to user userId=" + userId);
        }
        postRepository.deleteById(postId);
    }
}
