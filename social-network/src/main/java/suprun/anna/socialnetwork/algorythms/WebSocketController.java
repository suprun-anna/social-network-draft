package suprun.anna.socialnetwork.algorythms;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import suprun.anna.socialnetwork.dto.message.MessageDto;
import suprun.anna.socialnetwork.dto.message.MessageRequestDto;
import suprun.anna.socialnetwork.service.message.MessageService;

//@Controller
//@RequiredArgsConstructor
//public class WebSocketController {
//    private final MessageService messageService;
//
//    @MessageMapping("/chat")
//    @SendTo("/topic/messages")
//    public MessageDto sendMessage(MessageRequestDto messageDTO) {
//        return messageService.saveMessage(messageDTO);
//    }
//}
