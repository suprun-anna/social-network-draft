package suprun.anna.socialnetwork.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import suprun.anna.socialnetwork.model.BannedUser;

public interface BannedUserRepository extends JpaRepository<BannedUser, Long> {
}
