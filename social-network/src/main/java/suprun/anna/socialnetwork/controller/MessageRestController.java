package suprun.anna.socialnetwork.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import suprun.anna.socialnetwork.dto.message.MessageDto;
import suprun.anna.socialnetwork.service.message.DialogService;
import suprun.anna.socialnetwork.service.message.MessageService;

import java.util.List;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageRestController {
    private final MessageService messageService;
    private final DialogService dialogService;

    @GetMapping("/dialog/getAll")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public List<MessageDto> getAllMessagesFromDialog(@RequestParam Long dialogId, Pageable pageable) {
        return messageService.getAllMessagesBetweenUsersByDialogId(dialogId, pageable);
    }

    @GetMapping("/dialog")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public Long getDialog(@RequestParam Long user1Id, @RequestParam Long user2Id) {
        return dialogService.getDialogBetweenUsers(user1Id, user2Id);
    }

    @GetMapping("/allDialogs")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public List<Long> getAllDialogs(@RequestParam Long userId) {
        return dialogService.getAllDialogsByUserId(userId);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public List<MessageDto> getAllMessages(@RequestParam Long senderId, @RequestParam Long receiverId, Pageable pageable) {
        return messageService.getAllMessagesBetweenUsersByDialogId(senderId, receiverId, pageable);
    }

    @GetMapping("/last-message")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public MessageDto getLastMessage(@RequestParam Long dialogId) {
        System.out.println("LAST");
        return messageService.getLastMessageByDialogId(dialogId);
    }
}
