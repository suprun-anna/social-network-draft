package suprun.anna.socialnetwork.repository;

import java.util.Optional;
import suprun.anna.socialnetwork.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(Role.RoleName roleName);
}
