package suprun.anna.socialnetwork.service.user;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import suprun.anna.socialnetwork.dto.user.UserInfoResponseDto;
import suprun.anna.socialnetwork.dto.user.UserRedirectResponseDto;
import suprun.anna.socialnetwork.dto.user.UserRegistrationRequestDto;
import suprun.anna.socialnetwork.dto.user.UserResponseDto;
import suprun.anna.socialnetwork.model.User;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto userDto);

    UserResponseDto getByEmail(String email);

    User getById(Long id);

    User getByUsername(String username);

    UserResponseDto updateProfilePicture(User user, MultipartFile profilePicture) throws IOException;

    UserResponseDto updateBio(User user, String bio);

    UserResponseDto updateDisplayName(User user, String displayName);

    UserResponseDto updateAge(User user, int age);

    UserInfoResponseDto getUserInfo(User user);

    List<UserRedirectResponseDto> findByPartialUsername(String username, Pageable pageable);
}
