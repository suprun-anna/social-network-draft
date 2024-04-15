package suprun.anna.socialnetwork.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import suprun.anna.socialnetwork.dto.message.MessageDto;
import suprun.anna.socialnetwork.dto.message.MessageRequestDto;
import suprun.anna.socialnetwork.service.message.DialogService;
import suprun.anna.socialnetwork.service.message.MessageService;


@Controller
@RequiredArgsConstructor
@Slf4j
public class MessageController {
	private final SimpMessagingTemplate simpMessagingTemplate;
	private final MessageService messageService;
	private final DialogService dialogService;

	@MessageMapping("/chat")
	public void chat(@Payload MessageRequestDto message) {
		log.info("Message received: {}", message);
		Long senderId = message.senderId();
		Long receiverId = message.receiverId();
		Long dialogId = dialogService.getDialogBetweenUsers(senderId, receiverId);
		MessageDto savedMessage = messageService.saveMessage(message);
		String topic = "/topic/chat/" + dialogId.toString();
		simpMessagingTemplate.convertAndSend(topic, savedMessage);
	}
}