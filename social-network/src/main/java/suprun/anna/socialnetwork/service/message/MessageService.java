package suprun.anna.socialnetwork.service.message;

import suprun.anna.socialnetwork.dto.message.MessageDto;
import suprun.anna.socialnetwork.dto.message.MessageRequestDto;
import suprun.anna.socialnetwork.model.Message;

import java.util.List;

public interface MessageService {
    List<Message> getAllMessages();

    List<Message> getAllMessagesBetweenUsers(Long userId1, Long receiverId);

    MessageDto saveMessage(MessageRequestDto messageDto);
}
