package suprun.anna.socialnetwork.service.post;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import suprun.anna.socialnetwork.dto.post.PostDto;
import suprun.anna.socialnetwork.dto.post.PostUpdateDto;
import suprun.anna.socialnetwork.model.User;
import org.springframework.data.domain.Pageable;

public interface PostService {
    PostDto save(User user, String title, String content, MultipartFile picture) throws IOException;

    List<PostDto> findAll();

    List<PostDto> findAll(User user, Pageable pageable);

    List<PostDto> findAllByTimeOrder(Long id, Pageable pageable);

    PostDto getById(Long id);

    PostDto editById(Long userId, PostUpdateDto postUpdateDto);

    List<PostDto> findAllFromFollowings(Long userId, Pageable pageable);

    void deleteById(Long userId, Long postId);
}
