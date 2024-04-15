package suprun.anna.socialnetwork.service.message;

import org.springframework.data.domain.Pageable;
import suprun.anna.socialnetwork.dto.message.MessageDto;
import suprun.anna.socialnetwork.dto.message.MessageRequestDto;
import suprun.anna.socialnetwork.model.Message;

import java.util.List;

public interface MessageService {
    List<MessageDto> getAllMessages();

    List<MessageDto> getAllMessagesBetweenUsers(Long senderId, Long receiverId, Pageable pageable);

    MessageDto saveMessage(MessageRequestDto messageDto);

    MessageDto getLastMessage(Long senderId, Long receiverId);
}
