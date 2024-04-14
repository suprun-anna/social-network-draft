package suprun.anna.socialnetwork.repository.Message;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import suprun.anna.socialnetwork.model.Comment;
import suprun.anna.socialnetwork.model.Message;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long>, JpaSpecificationExecutor<Message> {
    @Query("SELECT m FROM Message m WHERE " +
            "(m.sender.id = :senderId AND m.receiver.id = :receiverId) " +
            "OR (m.sender.id = :receiverId AND m.receiver.id = :senderId) " +
            "ORDER BY m.sentAt DESC")
    List<Message> findAllMessagesBetweenUsers(Long senderId, Long receiverId, Pageable pageable);
}