package suprun.anna.socialnetwork.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import suprun.anna.socialnetwork.config.MapperConfig;
import suprun.anna.socialnetwork.dto.message.MessageDto;
import suprun.anna.socialnetwork.dto.message.MessageRequestDto;
import suprun.anna.socialnetwork.model.Message;

@Mapper(config = MapperConfig.class, uses = {UserMapper.class, EntityConversionService.class})
public interface MessageMapper {
    @Mapping(source = "sender.id", target = "senderId")
    @Mapping(source = "receiver.id", target = "receiverId")
    @Mapping(source = "sender.profilePicture", target = "senderAvatar")
    @Mapping(source = "receiver.profilePicture", target = "receiverAvatar")
    @Mapping(source = "sender", target = "senderName", qualifiedByName = "customUsernameGetter")
    @Mapping(source = "receiver", target = "receiverName", qualifiedByName = "customUsernameGetter")
    MessageDto toDto(Message message);

    @Mapping(source = "senderId", target = "sender", qualifiedByName = "userFromId")
    @Mapping(source = "receiverId", target = "receiver", qualifiedByName = "userFromId")
    Message toModel(MessageRequestDto messageDto);
}