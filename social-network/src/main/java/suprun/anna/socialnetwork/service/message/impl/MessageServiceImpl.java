package suprun.anna.socialnetwork.service.message.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import suprun.anna.socialnetwork.dto.message.MessageDto;
import suprun.anna.socialnetwork.dto.message.MessageRequestDto;
import suprun.anna.socialnetwork.mapper.MessageMapper;
import suprun.anna.socialnetwork.model.Message;
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

    public List<MessageDto> getAllMessagesBetweenUsersByDialogId(Long senderId, Long receiverId, Pageable pageable) {
        return messageRepository.findAllMessagesBetweenUsers(senderId, receiverId, pageable).stream()
                .map(messageMapper::toDto)
                .toList();
    }

    @Override
    public List<MessageDto> getAllMessagesBetweenUsersByDialogId(Long dialogId, Pageable pageable) {
        return messageRepository.findAllMessagesBetweenUsersByDialogId(dialogId, pageable).stream()
                .map(messageMapper::toDto)
                .toList();
    }

    public MessageDto saveMessage(MessageRequestDto messageDto) {
        System.out.println("Save message");
        Message message = messageMapper.toModel(messageDto);
        message.setSentAt(LocalDateTime.now());
        message = messageRepository.save(message);
        System.out.println(message);
        return messageMapper.toDto(message);
    }

    @Override
    public MessageDto getLastMessage(Long senderId, Long receiverId) {
        return messageMapper.toDto(messageRepository.findAllMessagesBetweenUsers(senderId, receiverId,
                PageRequest.of(0, 1)).get(0));
    }

    @Override
    public MessageDto getLastMessageByDialogId(Long dialogId) {
        List<Message> messages = messageRepository.findAllMessagesBetweenUsersByDialogId(dialogId,
                PageRequest.of(0, 1));
        return messages.size() > 0 ? messageMapper.toDto(messages.get(0)) : null;
    }
}
