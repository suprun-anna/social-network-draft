package suprun.anna.socialnetwork.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import suprun.anna.socialnetwork.dto.club.ClubDto;
import suprun.anna.socialnetwork.model.Club;
import suprun.anna.socialnetwork.model.User;

import java.util.List;
import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club, Long> {
    @Query("SELECT c FROM Club c " +
            "WHERE LOWER(c.name) LIKE CONCAT('%', LOWER(:name), '%')")
    List<Club> findByPartialName(String name, Pageable pageable);

    @Query("SELECT c FROM Club c " +
            "WHERE LOWER(c.name) LIKE CONCAT('%', LOWER(:name), '%') " +
            "AND c.owner.id = :userId")
    List<Club> findOwnedClubsPartialName(Long userId, String name, Pageable pageable);

    @Query("SELECT c FROM Club c " +
            "WHERE LOWER(c.name) LIKE CONCAT('%', LOWER(:name), '%') " +
            "AND c.isOpen = TRUE")
    List<Club> findOpenClubsByPartialName(String name, Pageable pageable);

    @Query("SELECT c FROM Club c " +
            "JOIN c.members u " +
            "WHERE u.id = :userId ")
    List<Club> findUserClubs(Long userId, Pageable pageable);

    @Query("SELECT c FROM Club c " +
            "JOIN c.members u " +
            "WHERE u.id = :userId AND LOWER(c.name) LIKE CONCAT('%', LOWER(:name), '%') ")
    List<Club> findUserClubsByName(Long userId, String name, Pageable pageable);

//    @Query("SELECT c FROM Club c " +
//            "JOIN c.members u " +
//            "WHERE u.id = :userId AND c.id = :clubId")
//    Club findUserClubById(Long userId, Long clubId);
//
//    @Query("SELECT c FROM Club c " +
//            "JOIN c.members u " +
//            "WHERE c.isOpen = TRUE AND c.id = :clubId")
//    Club findUserClubById(Long clubId);

    @Query("SELECT c FROM Club c " +
            "WHERE (c.id = :clubId AND " +
            "(EXISTS (SELECT u FROM c.members u WHERE u.id = :userId) " +
            "OR c.isOpen = TRUE))")
    Club findUserClubById(Long userId, Long clubId);

    @Query("SELECT c FROM Club c " +
            "WHERE c.id = :clubId AND " +
            "EXISTS (SELECT u FROM c.members u WHERE u.id = :userId)")
    Optional<Club> findClubMember(Long userId, Long clubId);

    @Query("SELECT u FROM User u " +
            "JOIN u.clubs c " +
            "WHERE c.id = :clubId")
    List<User> findClubMembersById(Long clubId, Pageable pageable);
}
