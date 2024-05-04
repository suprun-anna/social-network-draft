package suprun.anna.socialnetwork.mapper;

import suprun.anna.socialnetwork.config.MapperConfig;
import suprun.anna.socialnetwork.dto.post.PostDto;
import suprun.anna.socialnetwork.dto.post.PostUpdateDto;
import suprun.anna.socialnetwork.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class, uses = {LikeMapper.class, CommentMapper.class, ClubMapper.class})
public interface PostMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "isUpdated", target = "isUpdated")
    @Named("postsToDto")
    @Mapping(source = "club.id", target = "clubId")
    PostDto toDto(Post post);

    @Mapping(target = "user.id", ignore = true)
    @Mapping(target = "likes", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(source = "clubId", target = "club", qualifiedByName = "clubFromId")
    Post toModel(PostUpdateDto postUpdateDto);
}
