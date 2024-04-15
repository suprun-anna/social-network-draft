package suprun.anna.socialnetwork.controller;

import lombok.RequiredArgsConstructor;
import org.hibernate.query.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import suprun.anna.socialnetwork.dto.message.MessageDto;
import suprun.anna.socialnetwork.service.message.MessageService;

import java.util.List;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageRestController {
    private final MessageService messageService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<MessageDto> getAllMessages(@RequestParam Long senderId, @RequestParam Long receiverId, Pageable pageable) {
        return messageService.getAllMessagesBetweenUsers(senderId, receiverId, pageable);
    }

    @GetMapping("/last-message")
    @PreAuthorize("hasRole('ROLE_USER')")
    public MessageDto getLastMessage(@RequestParam Long senderId, @RequestParam Long receiverId) {
        return messageService.getLastMessage(senderId, receiverId);
    }
}
