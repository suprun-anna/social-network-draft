package suprun.anna.socialnetwork.mapper;

import suprun.anna.socialnetwork.config.MapperConfig;
import suprun.anna.socialnetwork.dto.post.like.LikeDto;
import suprun.anna.socialnetwork.model.Like;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class, uses = {EntityConversionService.class})
public interface LikeMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "post.id", target = "postId")
    @Named("likesToDto")
    LikeDto toDto(Like like);

    @Mapping(source = "userId", target = "user", qualifiedByName = "userFromId")
    @Mapping(source = "postId", target = "post", qualifiedByName = "postFromId")
    Like toModel(LikeDto likeDto);
}