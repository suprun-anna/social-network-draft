package suprun.anna.socialnetwork.algorythms;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import suprun.anna.socialnetwork.dto.message.MessageDto;
import suprun.anna.socialnetwork.dto.message.MessageRequestDto;
import suprun.anna.socialnetwork.model.Message;
import suprun.anna.socialnetwork.service.message.MessageService;

import java.util.List;


@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {
	private final MessageService messageService;

	@GetMapping
	public ResponseEntity<List<Message>> getAllMessages() {
		List<Message> messages = messageService.getAllMessages();
		return ResponseEntity.ok(messages);
	}

	@GetMapping("/between/{userId1}/{userId2}")
	public ResponseEntity<List<Message>> getAllMessagesBetweenUsers(
			@PathVariable Long userId1,
			@PathVariable Long userId2
	) {
		List<Message> messages = messageService.getAllMessagesBetweenUsers(userId1, userId2);
		return ResponseEntity.ok(messages);
	}

	@PostMapping
	public MessageDto saveMessage(@RequestBody MessageRequestDto message) {
		return messageService.saveMessage(message);
	}
}










//@RestController
//@RequiredArgsConstructor
//public class WebSocketTextController {
//	private final SimpMessagingTemplate template;
//
//	@PostMapping("/send")
//	public ResponseEntity<Void> sendMessage(@RequestBody TextMessageDTO textMessageDTO) {
//		template.convertAndSend("/topic/message", textMessageDTO);
//		return new ResponseEntity<>(HttpStatus.OK);
//	}
//
//	@MessageMapping("/sendMessage")
//	public void receiveMessage(@Payload TextMessageDTO textMessageDTO) {
//		// receive message from client
//	}
//
//
//	@SendTo("/topic/message")
//	public TextMessageDTO broadcastMessage(@Payload TextMessageDTO textMessageDTO) {
//		return textMessageDTO;
//	}
//}