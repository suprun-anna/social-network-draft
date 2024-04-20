package suprun.anna.socialnetwork.repository.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import suprun.anna.socialnetwork.model.Dialog;

import java.util.List;
import java.util.Optional;

public interface DialogRepository extends JpaRepository<Dialog, Long> {
    @Query("SELECT d FROM Dialog d WHERE " +
            "(d.user1.id = :userId OR d.user2.id = :userId) ")
    List<Dialog> findAllDialogsByUserId(Long userId);

    @Query("SELECT d FROM Dialog d WHERE " +
            "(d.user1.id = :user1Id AND d.user2.id = :user2Id) " +
            "OR (d.user1.id = :user2Id AND d.user2.id = :user1Id) ")
    Optional<Dialog> findDialogBetweenUsers(Long user1Id, Long user2Id);
}
