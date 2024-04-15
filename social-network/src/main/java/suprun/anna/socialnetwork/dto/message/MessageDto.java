package suprun.anna.socialnetwork.dto.message;

import java.time.LocalDateTime;

public record MessageDto(
        Long id,
        Long senderId,
        String senderName,
        String senderAvatar,
        Long receiverId,
        String receiverName,
        String receiverAvatar,
        String messageText,
        LocalDateTime sentAt,
        Long dialogId
        ) {
}
