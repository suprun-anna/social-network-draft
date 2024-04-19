package suprun.anna.socialnetwork.service.user.impl;

import jakarta.persistence.EntityNotFoundException;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import suprun.anna.socialnetwork.dto.user.UserInfoResponseDto;
import suprun.anna.socialnetwork.dto.user.UserRedirectResponseDto;
import suprun.anna.socialnetwork.dto.user.UserRegistrationRequestDto;
import suprun.anna.socialnetwork.dto.user.UserResponseDto;
import suprun.anna.socialnetwork.exception.RegistrationException;
import suprun.anna.socialnetwork.mapper.UserMapper;
import suprun.anna.socialnetwork.model.Role;
import suprun.anna.socialnetwork.model.User;
import suprun.anna.socialnetwork.repository.user.RoleRepository;
import suprun.anna.socialnetwork.repository.user.UserRepository;
import suprun.anna.socialnetwork.service.post.FileUploadService;
import suprun.anna.socialnetwork.service.user.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private static final Role.RoleName DEFAULT_ROLE_NAME = Role.RoleName.ROLE_USER;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final FileUploadService fileUploadService;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto userDto) {
        if (userRepository.findByEmail(userDto.email()).isPresent()
                || userRepository.findByUsername(userDto.username()).isPresent()) {
            throw new RegistrationException("Can't register user with email=" + userDto.email()
                    + " or username=" + userDto.username());
        }
        User user = userMapper.toModel(userDto);
        user.setDisplayName(user.getName());
        user.setPassword(passwordEncoder.encode(userDto.password()));
        Set<Role> userRoles = new HashSet<>();
        Role userRole = roleRepository.findByName(DEFAULT_ROLE_NAME)
                .orElseThrow(() -> new RegistrationException("Default user role not found"));
        userRoles.add(userRole);
        user.setRoles(userRoles);
        if (user.getProfilePicture() == null) {
            user.setProfilePicture("default_pfp.png");
        }
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserResponseDto getByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(userMapper::toDto).orElseThrow(
                () -> new EntityNotFoundException("Can't find user by email = " + email));
    }

    @Override
    public User getById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(
                () -> new EntityNotFoundException("Can't find user by id=" + id));
    }

    @Override
    public User getByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.orElseThrow(
                () -> new EntityNotFoundException("Can't find user by username = " + username));
    }

    @Override
    public UserResponseDto updateProfilePicture(User user, MultipartFile profilePicture) throws IOException {
        String path = fileUploadService.saveFile(profilePicture);
        user.setProfilePicture(path);
        try {
            user = userRepository.save(user);
        } catch (Exception e) {
            fileUploadService.deleteFile(path);
            throw new RuntimeException("Can't update pfp of user with id=" + user.getId());
        }
        return userMapper.toDto(user);
    }

    @Override
    public UserResponseDto updateBio(User user, String bio) {
        if (bio == null) bio = "";
        user.setBio(bio);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserResponseDto updateDisplayName(User user, String displayName) {
        if (displayName == null || displayName.trim().isBlank()) user.setDisplayName(user.getName());
        else user.setDisplayName(displayName);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserResponseDto updateAge(User user, int age) {
        user.setAge(age);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserInfoResponseDto getUserInfo(User user) {
        return userMapper.toInfoResponseDto(user);
    }

    @Override
    public List<UserRedirectResponseDto> findByPartialUsername(String username, Pageable pageable) {
        return userRepository.findByPartialUsername(username, pageable).stream()
                .map(userMapper::toRedirectResponseDto)
                .toList();
    }

    private boolean isValidUsername(String username) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]+(?:[_\\-.][a-zA-Z0-9]+)*$");
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }
}
