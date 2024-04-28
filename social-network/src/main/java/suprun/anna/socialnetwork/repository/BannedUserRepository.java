package suprun.anna.socialnetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import suprun.anna.socialnetwork.model.BannedUser;

public interface BannedUserRepository extends JpaRepository<BannedUser, Long> {
}
