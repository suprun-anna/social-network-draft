package suprun.anna.socialnetwork.service.message.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import suprun.anna.socialnetwork.model.Dialog;
import suprun.anna.socialnetwork.model.User;
import suprun.anna.socialnetwork.repository.message.DialogRepository;
import suprun.anna.socialnetwork.service.message.DialogService;
import suprun.anna.socialnetwork.service.user.UserService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DialogServiceImpl implements DialogService {
    private final DialogRepository dialogRepository;
    private final UserService userService;

    @Override
    public Long getDialogBetweenUsers(Long user1Id, Long user2Id) {
        Optional<Dialog> dialogBetweenUsers = dialogRepository.findDialogBetweenUsers(user1Id, user2Id);
        if (dialogBetweenUsers.isPresent()) {
            return dialogBetweenUsers.get().getId();
        } else {
            User user1 = userService.getById(user1Id);
            User user2 = userService.getById(user2Id);
            Dialog dialog = new Dialog();
            dialog.setUser1(user1);
            dialog.setUser2(user2);
            dialog = dialogRepository.save(dialog);
            return dialog.getId();
        }
    }

    @Override
    public List<Long> getAllDialogsByUserId(Long userId) {
        return dialogRepository.findAllDialogsByUserId(userId).stream()
                .map(Dialog::getId)
                .toList();
    }
}
