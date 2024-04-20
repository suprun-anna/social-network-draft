package suprun.anna.socialnetwork.repository.group;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import suprun.anna.socialnetwork.model.Group;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long>, JpaSpecificationExecutor<Group> {
    @Query("SELECT g FROM Group g " +
            "WHERE LOWER(g.name) LIKE CONCAT('%', LOWER(:name), '%')")
    List<Group> findByPartialName(String name, Pageable pageable);

    @Query("SELECT g FROM Group g " +
            "WHERE LOWER(g.name) LIKE CONCAT('%', LOWER(:name), '%') " +
            "AND g.isOpen = TRUE")
    List<Group> findOpenGroupsByPartialName(String name, Pageable pageable);

    @Query("SELECT g FROM Group g " +
            "JOIN g.followers u " +
            "WHERE u.id = :userId")
    List<Group> findUserGroupsByPartialName(Long userId, Pageable pageable);

}
