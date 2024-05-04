package suprun.anna.socialnetwork.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import suprun.anna.socialnetwork.config.MapperConfig;
import suprun.anna.socialnetwork.dto.club.ClubDto;
import suprun.anna.socialnetwork.dto.club.ClubCreateDto;
import suprun.anna.socialnetwork.dto.club.ClubRedirectResponseDto;
import suprun.anna.socialnetwork.model.Club;

import java.util.Optional;

@Mapper(config = MapperConfig.class, uses = {EntityConversionService.class, PostMapper.class})
public interface ClubMapper {
    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "isOpen", target = "isOpen")
    ClubDto toDto(Club club);

    @Mapping(source = "isOpen", target = "isOpen")
    Club toModel(ClubCreateDto clubCreateDto);

    ClubRedirectResponseDto toRedirectResponseDto(Club club);

    @Named("clubFromId")
    default Club clubFromId(Long id) {
        return Optional.ofNullable(id)
                .map(Club::new)
                .orElse(null);
    }
}
