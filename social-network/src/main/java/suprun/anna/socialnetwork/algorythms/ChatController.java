package suprun.anna.socialnetwork.algorythms;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/send-message")
    public void sendMessage(@Payload String message) {
        // Отправить сообщение всем подписчикам на канале "/topic/messages"
        messagingTemplate.convertAndSend("/topic/messages", message);
    }
}