package suprun.anna.socialnetwork.dto.message;

public record MessageRequestDto(
        Long senderId,
        Long receiverId,
        String messageText,
        Long dialogId
) {
}
