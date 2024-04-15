package suprun.anna.socialnetwork.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import suprun.anna.socialnetwork.config.MapperConfig;
import suprun.anna.socialnetwork.dto.message.MessageDto;
import suprun.anna.socialnetwork.dto.message.MessageRequestDto;
import suprun.anna.socialnetwork.model.Dialog;
import suprun.anna.socialnetwork.model.Message;

import java.util.Optional;

@Mapper(config = MapperConfig.class, uses = {UserMapper.class, EntityConversionService.class})
public interface MessageMapper {
    @Mapping(source = "sender.id", target = "senderId")
    @Mapping(source = "receiver.id", target = "receiverId")
    @Mapping(source = "sender.profilePicture", target = "senderAvatar")
    @Mapping(source = "receiver.profilePicture", target = "receiverAvatar")
    @Mapping(source = "sender", target = "senderName", qualifiedByName = "customUsernameGetter")
    @Mapping(source = "receiver", target = "receiverName", qualifiedByName = "customUsernameGetter")
    @Mapping(source = "dialog.id", target = "dialogId")
    MessageDto toDto(Message message);

    @Mapping(source = "senderId", target = "sender", qualifiedByName = "userFromId")
    @Mapping(source = "receiverId", target = "receiver", qualifiedByName = "userFromId")
    @Mapping(source = "dialogId", target = "dialog", qualifiedByName = "dialogFromId")
    Message toModel(MessageRequestDto messageDto);

    @Named("dialogFromId")
    default Dialog dialogFromId(Long id) {
        return Optional.ofNullable(id)
                .map(Dialog::new)
                .orElse(null);
    }
}