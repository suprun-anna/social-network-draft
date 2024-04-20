package suprun.anna.socialnetwork.service.group.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import suprun.anna.socialnetwork.dto.group.GroupCreateDto;
import suprun.anna.socialnetwork.dto.group.GroupDto;
import suprun.anna.socialnetwork.repository.group.GroupRepository;
import suprun.anna.socialnetwork.service.group.GroupService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;

    @Override
    public GroupDto save(GroupCreateDto groupCreateDto) {
        return null;
    }

    @Override
    public GroupDto update(GroupCreateDto groupCreateDto) {
        return null;
    }

    @Override
    public void delete(Long groupId) {
        groupRepository.deleteById(groupId);
    }

    @Override
    public List<GroupDto> findByPartialName(String name, Pageable pageable) {
        return null;
    }
}
