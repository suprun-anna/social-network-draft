package suprun.anna.socialnetwork.service.message;

import suprun.anna.socialnetwork.model.Dialog;

import java.util.List;

public interface DialogService {
    Long findDialogBetweenUsers(Long user1Id, Long user2Id);

    List<Long> findAllDialogsByUserId(Long userId);

}
