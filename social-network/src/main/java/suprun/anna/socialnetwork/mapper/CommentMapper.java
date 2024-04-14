package suprun.anna.socialnetwork.mapper;

import suprun.anna.socialnetwork.config.MapperConfig;
import suprun.anna.socialnetwork.dto.post.comment.CommentDto;
import suprun.anna.socialnetwork.dto.post.comment.CommentRequestDto;
import suprun.anna.socialnetwork.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class, uses = {EntityConversionService.class})
public interface CommentMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "post.id", target = "postId")
    @Mapping(source = "text", target = "text")
    @Named("commentsToDto")
    CommentDto toDto(Comment comment);

    @Mapping(source = "userId", target = "user", qualifiedByName = "userFromId")
    @Mapping(source = "postId", target = "post", qualifiedByName = "postFromId")
    Comment toModel(CommentRequestDto commentDto);
}

