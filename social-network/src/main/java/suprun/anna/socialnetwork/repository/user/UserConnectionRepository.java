package suprun.anna.socialnetwork.repository.user;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import suprun.anna.socialnetwork.model.User;
import suprun.anna.socialnetwork.model.UserConnection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserConnectionRepository extends JpaRepository<UserConnection, Long>, JpaSpecificationExecutor<UserConnection> {
    @Query("SELECT c.follower FROM UserConnection c " +
            "WHERE c.user.id = :userId AND c.isDeleted = FALSE " +
            "ORDER BY c.createdAt DESC")
    List<User> getAllFollowers(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT c.user FROM UserConnection c " +
            "WHERE c.follower.id = :userId AND c.isDeleted = FALSE " +
            "ORDER BY c.createdAt DESC")
    List<User> getAllFollowings(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT c FROM UserConnection c " +
            "WHERE c.user.id = :userId AND c.follower.id = :followerId " +
            "AND (c.isDeleted = FALSE OR c.isDeleted = TRUE)")
    Optional<UserConnection> getConnection(@Param("userId") Long userId, @Param("followerId") Long followerId);
}