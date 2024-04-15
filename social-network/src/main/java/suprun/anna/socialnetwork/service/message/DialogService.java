package suprun.anna.socialnetwork.service.message;

import java.util.List;

public interface DialogService {
//    Long createDialog(Long user1Id, Long user2Id);

    Long getDialogBetweenUsers(Long user1Id, Long user2Id);

    List<Long> getAllDialogsByUserId(Long userId);
}
