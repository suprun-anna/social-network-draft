package suprun.anna.socialnetwork.mapper;

import org.mapstruct.Mapping;
import suprun.anna.socialnetwork.config.MapperConfig;
import suprun.anna.socialnetwork.dto.userconnection.UserConnectionDto;
import suprun.anna.socialnetwork.dto.user.UserInfoResponseDto;
import suprun.anna.socialnetwork.dto.user.UserRedirectResponseDto;
import suprun.anna.socialnetwork.dto.user.UserRegistrationRequestDto;
import suprun.anna.socialnetwork.dto.user.UserResponseDto;
import suprun.anna.socialnetwork.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import suprun.anna.socialnetwork.model.UserConnection;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    @Named("usersToDto")
    @Mapping(source="user", target = "username", qualifiedByName = "customUsernameGetter")
    UserResponseDto toDto(User user);

    User toModel(UserRegistrationRequestDto userDto);

    @Named("customUsernameGetter")
    default String customGetUsername(User user) {
        return user.getName();
    }

    @Mapping(source="user", target = "username", qualifiedByName = "customUsernameGetter")
    UserInfoResponseDto toInfoResponseDto(User user);

    @Mapping(source="user", target = "username", qualifiedByName = "customUsernameGetter")
    UserRedirectResponseDto toRedirectResponseDto(User user);

    @Mapping(source="userConnection.user.id", target = "userId")
    @Mapping(source="userConnection.follower.id", target = "followerId")
    UserConnectionDto userConnectionToDto(UserConnection userConnection);
}
