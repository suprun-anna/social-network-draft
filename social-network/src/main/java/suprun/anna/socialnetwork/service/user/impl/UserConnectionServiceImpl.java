package suprun.anna.socialnetwork.service.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import suprun.anna.socialnetwork.dto.user.UserResponseDto;
import suprun.anna.socialnetwork.dto.userconnection.UserConnectionDto;
import suprun.anna.socialnetwork.dto.user.UserRedirectResponseDto;
import suprun.anna.socialnetwork.mapper.UserMapper;
import suprun.anna.socialnetwork.model.User;
import suprun.anna.socialnetwork.model.UserConnection;
import suprun.anna.socialnetwork.repository.user.UserConnectionRepository;
import suprun.anna.socialnetwork.repository.user.UserRepository;
import suprun.anna.socialnetwork.service.user.UserConnectionService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserConnectionServiceImpl implements UserConnectionService {
    private final UserConnectionRepository userConnectionRepository;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final Random random = new Random();

    @Override
    public List<UserRedirectResponseDto> getAllFollowers(Long id, Pageable pageable) {
        return userConnectionRepository.getAllFollowers(id, pageable).stream()
                .map(userMapper::toRedirectResponseDto)
                .toList();
    }

    @Override
    public Set<UserResponseDto> getRandomFollowersOfFollowings(User user) {
        Set<User> result = new HashSet<>();
        int pageSize = 10;
        List<User> followings = getRandomFollowings(user, pageSize);
        if (followings.isEmpty()) {
            return getRandomUsers(followings).stream().map(userMapper::toDto).collect(Collectors.toSet());
        }
        System.out.println(11111111);
        for (User u : followings){
            System.out.println(u.getName() + " " + u.getId());
        }
        System.out.println();
        for (User randomFollowing : followings) {
            List<User> randomFollowers = getRandomFollowings(randomFollowing, pageSize);
            int size = randomFollowers.size();
            int from, to;
            if (size == 0) continue;
            else if (size == 1 || size == 2) {
                from = size / 2;
                to = 1;
            } else {
                from = random.nextInt(randomFollowers.size() - 2);
                to = random.nextInt(from + 1, randomFollowers.size());
            }
            List<User> users = randomFollowers.subList(from, to);
            result.addAll(users);
        }
        if (result.size() < 5) {
            result.addAll(getRandomUsers(followings));
        }
        System.out.println(222222);
        for (User u : result){
            System.out.println(u.getName() + " " + u.getId());
        }
        System.out.println(11111111);

        return result.stream()
                .filter(u -> followings.stream().noneMatch(f -> f.getId().equals(u.getId())))
                .map(userMapper::toDto)
                .collect(Collectors.toSet());
    }

    private List<User> getRandomFollowings(User user, int pageSize){
        int totalFollowers = user.getFollowingCount();
        int randomPageNumber = (int) (Math.random() * (totalFollowers / 10));
        Pageable pageable = PageRequest.of(randomPageNumber, pageSize);
        return userConnectionRepository.getAllFollowings(user.getId(), pageable);
    }

    private List<User> getRandomUsers(List<User> existingUsers){
        List<User> all = userRepository.findAll();
        int size = all.size();
        int from = size > 10 ? random.nextInt( size - 10) : 0;
        int to = size > 10 ? random.nextInt(from + 10) : size;
        List<User> users = all.subList(from, to);
        users.removeAll(existingUsers);
        return users;
    }

    @Override
    public List<UserRedirectResponseDto> getAllFollowings(Long id, Pageable pageable) {
        return userConnectionRepository.getAllFollowings(id, pageable).stream()
                .map(userMapper::toRedirectResponseDto).toList();
    }

    @Override
    public boolean isFollower(Long userId, Long followerId){
        Optional<UserConnection> connection = userConnectionRepository.getConnection(userId, followerId);
        return connection.isPresent() && !connection.get().isDeleted();
    }

    @Override
    public UserConnection getConnection(Long userId, Long followerId) {
        Optional<UserConnection> connection = userConnectionRepository.getConnection(userId, followerId);
        if (connection.isPresent() && !connection.get().isDeleted())
            return connection.get();
        else throw new RuntimeException("No connection between userId=" + userId
                + " and followerId=" + followerId);
    }

    @Override
    public UserConnection.Connection getConnectionStatus(Long userId, Long followerId) {
        boolean isFollower = isFollower(userId, followerId);
        boolean isFollowing = isFollower(followerId, userId);
        return (isFollower && isFollowing) ? UserConnection.Connection.FRIENDS :
                isFollower ? UserConnection.Connection.IS_FOLLOWER :
                        isFollowing ? UserConnection.Connection.IS_FOLLOWING : UserConnection.Connection.NONE;
    }

    @Override
    @Transactional
    public UserConnectionDto followUser(Long userId, Long followerId) {
        Optional<UserConnection> connection = userConnectionRepository.getConnection(userId, followerId);
        UserConnection userConnection;
        if (userId.equals(followerId)) {
            throw new RuntimeException("Can't follow self");
        }
        if (connection.isEmpty() || connection.get().isDeleted()) {
            if (connection.isEmpty()) {
                userConnection = new UserConnection();
            } else {
                userConnection = connection.get();
                userConnection.setDeleted(false);
            }
            User user = userRepository.findById(userId).orElseThrow(
                    () -> new NoSuchElementException("No user with id=" + userId));
            User follower = userRepository.findById(followerId).orElseThrow(
                    () -> new NoSuchElementException("No user with id=" + followerId));
            userConnection.setUser(user);
            userConnection.setFollower(follower);
            userConnection.setCreatedAt(LocalDateTime.now());
            user.setFollowerCount(user.getFollowerCount() + 1);
            follower.setFollowingCount(follower.getFollowingCount() + 1);
            userRepository.save(user);
            userRepository.save(follower);
            return userMapper.userConnectionToDto(userConnectionRepository.save(userConnection));
        } else {
            throw new RuntimeException("User is already followed");
        }
    }

    @Override
    @Transactional
    public boolean unfollowUser(Long userId, Long followerId) {
        Optional<UserConnection> connection = userConnectionRepository.getConnection(userId, followerId);
        if (connection.isPresent()) {
            User user = userRepository.findById(userId).orElseThrow(
                    () -> new NoSuchElementException("No user with id=" + userId));
            User follower = userRepository.findById(followerId).orElseThrow(
                    () -> new NoSuchElementException("No user with id=" + followerId));
            user.setFollowerCount(user.getFollowerCount() - 1);
            follower.setFollowingCount(follower.getFollowingCount() - 1);
            userRepository.save(user);
            userRepository.save(follower);
            connection.get().setDeleted(true);
            return true;
        } else {
            throw new RuntimeException("No connection between userId=" + userId + " and followerId=" + followerId);
        }
    }
}
