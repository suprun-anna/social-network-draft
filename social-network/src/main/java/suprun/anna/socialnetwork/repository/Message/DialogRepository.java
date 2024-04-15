package suprun.anna.socialnetwork.repository.Message;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import suprun.anna.socialnetwork.model.Dialog;
import suprun.anna.socialnetwork.model.Message;

import java.util.List;

public interface DialogRepository extends JpaRepository<Dialog, Long> {
    @Query("SELECT d FROM Dialog d WHERE " +
            "(d.user1.id = :userId OR d.user2.id = :userId) " +
            "ORDER BY m.sentAt DESC")
    List<Dialog> findAllDialogsByUserId(Long userId);

    @Query("SELECT d FROM Dialog d WHERE " +
            "(d.user1.id = :senderId AND m.receiver.id = :user2Id) " +
            "OR (d.user1.id = :user2Id AND m.receiver.id = :user1Id) " +
            "ORDER BY m.sentAt DESC")
    Dialog findDialogBetweenUsers(Long user1Id, Long user2Id);
}
