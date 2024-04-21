package suprun.anna.socialnetwork.service.group.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import suprun.anna.socialnetwork.dto.group.ClubCreateDto;
import suprun.anna.socialnetwork.dto.group.ClubDto;
import suprun.anna.socialnetwork.dto.group.ClubUpdateDto;
import suprun.anna.socialnetwork.mapper.ClubMapper;
import suprun.anna.socialnetwork.model.Club;
import suprun.anna.socialnetwork.model.User;
import suprun.anna.socialnetwork.repository.group.ClubRepository;
import suprun.anna.socialnetwork.service.group.ClubService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService {
    private final ClubRepository groupRepository;
    private final ClubMapper clubMapper;

    @Override
    public ClubDto save(User user, ClubCreateDto clubCreateDto) {
        Club club = clubMapper.toModel(clubCreateDto);
        club.setOwner(user);
        if (club.getProfilePicture() == null) {
            club.setProfilePicture("default_club_pfp.png");
        }
        return clubMapper.toDto(groupRepository.save(club));
    }

    @Override
    public ClubDto update(User user, ClubUpdateDto clubUpdateDto) {
        Optional<Club> groupOptional = groupRepository.findById(clubUpdateDto.id());
        if (groupOptional.isEmpty() || !groupOptional.get().getOwner().getId().equals(user.getId())) {
            throw new NoSuchElementException();
        }
        Club club = groupOptional.get();
        club.setDescription(clubUpdateDto.description());
        club.setName(clubUpdateDto.name());
        club.setIsOpen(clubUpdateDto.isOpen());
        return clubMapper.toDto(groupRepository.save(club));
    }

    @Override
    public void delete(User user, Long clubId) {
        groupRepository.findById(clubId).orElseThrow(NoSuchElementException::new);
        groupRepository.deleteById(clubId);
    }

    @Override
    public List<ClubDto> findByPartialName(String name, Pageable pageable) {
        return groupRepository.findByPartialName(name, pageable).stream()
                .map(clubMapper::toDto)
                .toList();
    }
}
