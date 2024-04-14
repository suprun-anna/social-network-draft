package suprun.anna.socialnetwork.repository.user;

import java.util.Optional;
import suprun.anna.socialnetwork.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);
}
