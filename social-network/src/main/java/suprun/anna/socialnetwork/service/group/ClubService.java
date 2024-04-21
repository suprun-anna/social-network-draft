package suprun.anna.socialnetwork.service.group;

import org.springframework.data.domain.Pageable;
import suprun.anna.socialnetwork.dto.group.ClubDto;
import suprun.anna.socialnetwork.dto.group.ClubCreateDto;
import suprun.anna.socialnetwork.dto.group.ClubUpdateDto;
import suprun.anna.socialnetwork.model.User;

import java.util.List;

public interface ClubService {
    ClubDto save(User user, ClubCreateDto clubCreateDto);

    ClubDto update(User user, ClubUpdateDto clubUpdateDto);

    void delete(User user, Long clubId);

    List<ClubDto> findByPartialName(String name, Pageable pageable);
}
