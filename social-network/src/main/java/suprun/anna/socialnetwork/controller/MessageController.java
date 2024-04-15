package suprun.anna.socialnetwork.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import suprun.anna.socialnetwork.dto.message.MessageDto;
import suprun.anna.socialnetwork.dto.message.MessageRequestDto;
import suprun.anna.socialnetwork.model.Message;
import suprun.anna.socialnetwork.service.message.DialogService;
import suprun.anna.socialnetwork.service.message.MessageService;
import suprun.anna.socialnetwork.service.user.UserService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Controller
@RequiredArgsConstructor
@Slf4j
public class MessageController {
	private final SimpMessagingTemplate simpMessagingTemplate;
	private final UserService userService;
	private final MessageService messageService;
	private final DialogService dialogService;

	@MessageMapping("/chat")
	public void chat(@Payload MessageRequestDto message) {
		log.info("Message received: {}", message);
		MessageDto savedMessage = messageService.saveMessage(message);
		Long senderId = savedMessage.senderId();
		Long receiverId = savedMessage.receiverId();
		String topic = getUniqueTopic(senderId, receiverId);
		simpMessagingTemplate.convertAndSend(topic, savedMessage);
	}

	private String getUniqueTopic(Long userId1, Long userId2) {
		List<Long> sortedIds = Arrays.asList(userId1, userId2);
		Collections.sort(sortedIds);
		return String.format("/topic/chat/%d-%d", sortedIds.get(0), sortedIds.get(1));
	}
}