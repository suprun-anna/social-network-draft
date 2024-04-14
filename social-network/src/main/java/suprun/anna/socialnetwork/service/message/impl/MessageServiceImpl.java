package suprun.anna.socialnetwork.service.message.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import suprun.anna.socialnetwork.dto.message.MessageDto;
import suprun.anna.socialnetwork.dto.message.MessageRequestDto;
import suprun.anna.socialnetwork.mapper.MessageMapper;
import suprun.anna.socialnetwork.model.Message;
import suprun.anna.socialnetwork.model.User;
import suprun.anna.socialnetwork.repository.Message.MessageRepository;
import suprun.anna.socialnetwork.service.message.MessageService;
import suprun.anna.socialnetwork.service.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final UserService userService;

    public List<MessageDto> getAllMessages() {
        return messageRepository.findAll().stream()
                .map(messageMapper::toDto)
                .toList();
    }

    public List<MessageDto> getAllMessagesBetweenUsers(Long userId1, Long receiverId, Pageable pageable) {
        return messageRepository.findAllMessagesBetweenUsers(userId1, receiverId, pageable).stream()
                .map(messageMapper::toDto)
                .toList();
    }

    public MessageDto saveMessage(MessageRequestDto messageDto) {
        System.out.println("Save message");
        Message message = messageMapper.toModel(messageDto);
        message.setSentAt(LocalDateTime.now());
        User sender = userService.getById(messageDto.senderId());
        User receiver = userService.getById(messageDto.receiverId());
        message.setSender(sender);
        message.setReceiver(receiver);
        message = messageRepository.save(message);
        System.out.println(message);
        return messageMapper.toDto(message);
    }
}
