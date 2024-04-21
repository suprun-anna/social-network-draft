package suprun.anna.socialnetwork.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import suprun.anna.socialnetwork.config.MapperConfig;
import suprun.anna.socialnetwork.dto.group.ClubDto;
import suprun.anna.socialnetwork.dto.group.ClubCreateDto;
import suprun.anna.socialnetwork.model.Club;

@Mapper(config = MapperConfig.class, uses = {EntityConversionService.class, PostMapper.class})
public interface ClubMapper {
    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "isOpen", target = "isOpen")
    ClubDto toDto(Club club);

    @Mapping(source = "isOpen", target = "isOpen")
    Club toModel(ClubCreateDto clubCreateDto);
}
