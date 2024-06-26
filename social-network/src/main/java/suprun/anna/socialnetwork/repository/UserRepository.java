package suprun.anna.socialnetwork.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import suprun.anna.socialnetwork.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u JOIN u.roles r " +
            "WHERE u.email = :email ")
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name != 'ROLE_ADMIN' " +
            "AND u.email = :email ")
    Optional<User> findByUsersEmail(String email);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name != 'ROLE_ADMIN' " +
            "AND u.username = :username ")
    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name != 'ROLE_ADMIN'")
    List<User> findAll();

    @Query("SELECT u FROM User u JOIN u.roles r " +
            "WHERE r.name != 'ROLE_ADMIN' " +
            "AND LOWER(u.username) LIKE " +
            "CONCAT('%', LOWER(:username), '%')")
    List<User> findByPartialUsername(String username, Pageable pageable);
}
