package suprun.anna.socialnetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import suprun.anna.socialnetwork.model.BannedUser;
import suprun.anna.socialnetwork.model.User;

import java.util.Optional;

public interface BannedUserRepository extends JpaRepository<BannedUser, Long> {
    @Query("SELECT b FROM BannedUser b " +
            "WHERE b.unbanned = FALSE AND b.user.id = :id ")
    Optional<BannedUser> checkIfBannedById(Long id);
}
