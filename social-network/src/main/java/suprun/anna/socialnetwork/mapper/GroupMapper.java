package suprun.anna.socialnetwork.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import suprun.anna.socialnetwork.config.MapperConfig;
import suprun.anna.socialnetwork.dto.group.GroupCreateDto;
import suprun.anna.socialnetwork.dto.group.GroupDto;
import suprun.anna.socialnetwork.model.Group;
import suprun.anna.socialnetwork.model.Post;

@Mapper(config = MapperConfig.class, uses = {EntityConversionService.class, PostMapper.class})
public interface GroupMapper {
    @Mapping(source = "owner.id", target = "ownerId")
    GroupDto toDto(Group group);

    @Mapping(source = "ownerId", target = "owner", qualifiedByName = "userFromId")
    Group toModel(GroupCreateDto groupCreateDto);
}
