package suprun.anna.socialnetwork.repository.group;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import suprun.anna.socialnetwork.model.Club;

import java.util.List;

public interface ClubRepository extends JpaRepository<Club, Long>, JpaSpecificationExecutor<Club> {
    @Query("SELECT c FROM Club c " +
            "WHERE LOWER(c.name) LIKE CONCAT('%', LOWER(:name), '%')")
    List<Club> findByPartialName(String name, Pageable pageable);

    @Query("SELECT c FROM Club c " +
            "WHERE LOWER(c.name) LIKE CONCAT('%', LOWER(:name), '%') " +
            "AND c.isOpen = TRUE")
    List<Club> findOpenGroupsByPartialName(String name, Pageable pageable);

    @Query("SELECT c FROM Club c " +
            "JOIN c.members u " +
            "WHERE u.id = :userId")
    List<Club> findUserGroupsByPartialName(Long userId, Pageable pageable);

}
