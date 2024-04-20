package suprun.anna.socialnetwork.service.group;

import org.springframework.data.domain.Pageable;
import suprun.anna.socialnetwork.dto.group.GroupCreateDto;
import suprun.anna.socialnetwork.dto.group.GroupDto;

import java.util.List;

public interface GroupService {
    GroupDto save(GroupCreateDto groupCreateDto);

    GroupDto update(GroupCreateDto groupCreateDto);

    void delete(Long groupId);

    List<GroupDto> findByPartialName(String name, Pageable pageable);
}
