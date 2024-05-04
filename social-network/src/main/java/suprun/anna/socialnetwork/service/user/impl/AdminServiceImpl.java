package suprun.anna.socialnetwork.service.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import suprun.anna.socialnetwork.model.BannedUser;
import suprun.anna.socialnetwork.repository.BannedUserRepository;
import suprun.anna.socialnetwork.repository.UserRepository;
import suprun.anna.socialnetwork.service.user.AdminService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final BannedUserRepository bannedUserRepository;

    @Override
    public void banUser(Long userId, String reason) {
        BannedUser bannedUser = new BannedUser();
        bannedUser.setUser(userRepository.findById(userId).orElseThrow(RuntimeException::new));
        bannedUser.setBannedAt(LocalDateTime.now());
        bannedUser.setBanReason(reason);
        bannedUser.setUnbanned(false);
        bannedUserRepository.save(bannedUser);
    }

    @Override
    public boolean checkIfBannedById(Long userId) {
        Optional<BannedUser> bannedUserOptional = bannedUserRepository.checkIfBannedById(userId);
        return bannedUserOptional.isPresent();
    }

    @Override
    public void unbanUser(Long userId) {
        Optional<BannedUser> bannedUserOptional = bannedUserRepository.checkIfBannedById(userId);
        if(bannedUserOptional.isPresent()) {
            BannedUser bannedUser = bannedUserOptional.get();
            bannedUser.setUnbanned(true);
            bannedUserRepository.save(bannedUser);
        }
    }
}

