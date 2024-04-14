package suprun.anna.socialnetwork.mapper;

import java.util.Optional;
import suprun.anna.socialnetwork.config.MapperConfig;
import suprun.anna.socialnetwork.model.Post;
import suprun.anna.socialnetwork.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)

public interface EntityConversionService {
    @Named("postFromId")
    default Post postFromId(Long id) {
        return Optional.ofNullable(id)
                .map(Post::new)
                .orElse(null);
    }

    @Named("userFromId")
    default User userFromId(Long id) {
        return Optional.ofNullable(id)
                .map(User::new)
                .orElse(null);
    }
}
