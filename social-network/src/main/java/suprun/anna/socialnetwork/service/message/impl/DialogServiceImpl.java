package suprun.anna.socialnetwork.service.message.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import suprun.anna.socialnetwork.model.Dialog;
import suprun.anna.socialnetwork.repository.Message.DialogRepository;
import suprun.anna.socialnetwork.service.message.DialogService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DialogServiceImpl implements DialogService {
    private final DialogRepository dialogRepository;

    @Override
    public Long findDialogBetweenUsers(Long user1Id, Long user2Id) {
        return dialogRepository.findDialogBetweenUsers(user1Id, user2Id).getId();
    }

    @Override
    public List<Long> findAllDialogsByUserId(Long userId) {
        return dialogRepository.findAllDialogsByUserId(userId).stream()
                .map(Dialog::getId)
                .toList();
    }
}
