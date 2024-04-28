package suprun.anna.socialnetwork.service.club.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import suprun.anna.socialnetwork.dto.club.ClubCreateDto;
import suprun.anna.socialnetwork.dto.club.ClubDto;
import suprun.anna.socialnetwork.dto.club.ClubRedirectResponseDto;
import suprun.anna.socialnetwork.dto.club.ClubUpdateDto;
import suprun.anna.socialnetwork.dto.user.UserRedirectResponseDto;
import suprun.anna.socialnetwork.mapper.ClubMapper;
import suprun.anna.socialnetwork.mapper.UserMapper;
import suprun.anna.socialnetwork.model.Club;
import suprun.anna.socialnetwork.model.User;
import suprun.anna.socialnetwork.repository.ClubRepository;
import suprun.anna.socialnetwork.repository.UserRepository;
import suprun.anna.socialnetwork.service.club.ClubService;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService {
    private final ClubRepository clubRepository;
    private final ClubMapper clubMapper;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public ClubDto save(User user, ClubCreateDto clubCreateDto) {
        Club club = clubMapper.toModel(clubCreateDto);
        club.setOwner(user);
        club.setMembers(new HashSet<>());
        club.getMembers().add(user);
        club.setMemberCount(club.getMemberCount() +1);
        if (club.getProfilePicture() == null) {
            club.setProfilePicture("default_club_pfp.png");
        }
        return clubMapper.toDto(clubRepository.save(club));
    }

    @Override
    public ClubDto update(User user, ClubUpdateDto clubUpdateDto) {
        Optional<Club> groupOptional = clubRepository.findById(clubUpdateDto.id());
        if (groupOptional.isEmpty() || !groupOptional.get().getOwner().getId().equals(user.getId())) {
            throw new NoSuchElementException();
        }
        Club club = groupOptional.get();
        if(clubUpdateDto.name() != null && !clubUpdateDto.name().isBlank()) {
            club.setName(clubUpdateDto.name());
        }
        club.setDescription(clubUpdateDto.description() == null ? "" :clubUpdateDto.description());
        club.setIsOpen(clubUpdateDto.isOpen());
        return clubMapper.toDto(clubRepository.save(club));
    }

    @Override
    public void delete(User user, Long clubId) {
        clubRepository.findById(clubId).orElseThrow(NoSuchElementException::new);
        clubRepository.deleteById(clubId);
    }

    @Override
    public List<ClubRedirectResponseDto> findByPartialName(String name, Pageable pageable) {
        return clubRepository.findByPartialName(name, pageable).stream()
                .map(clubMapper::toRedirectResponseDto)
                .toList();
    }

    @Override
    public List<ClubRedirectResponseDto> findOwnedClubsPartialName(Long userId, String name, Pageable pageable) {
        return clubRepository.findOwnedClubsPartialName(userId, name, pageable).stream()
                .map(clubMapper::toRedirectResponseDto)
                .toList();
    }

    @Override
    public List<ClubRedirectResponseDto> findOpenClubsByPartialName(String name, Pageable pageable) {
        return clubRepository.findOpenClubsByPartialName(name, pageable).stream()
                .map(clubMapper::toRedirectResponseDto)
                .toList();
    }

    @Override
    public List<ClubDto> findUserClubs(Long userId, Pageable pageable) {
        return clubRepository.findUserClubs(userId, pageable).stream()
                .map(clubMapper::toDto)
                .toList();
    }

    @Override
    public List<ClubDto> findUserClubsByName(Long userId, String name, Pageable pageable) {
        return clubRepository.findUserClubsByName(userId, name, pageable).stream()
                .map(clubMapper::toDto)
                .toList();
    }

    @Override
    public ClubDto findClubById(Long userId, Long clubId) {
        return clubMapper.toDto(clubRepository.findUserClubById(userId, clubId));
    }

    @Override
    public boolean checkClubMemberExistence(Long userId, Long clubId) {
        return clubRepository.findClubMember(userId, clubId).isPresent();
    }

    @Override
    public List<UserRedirectResponseDto> findClubMembers(Long clubId, Pageable pageable) {
        return clubRepository.findClubMembersById(clubId, pageable).stream()
                .map(userMapper::toRedirectResponseDto)
                .toList();
    }

    @Override
    public void joinClub(Long userId, Long clubId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new EntityNotFoundException("Club not found with id: " + clubId));

        if (!club.getMembers().contains(user)) {
            club.getMembers().add(user);
            club.setMemberCount(club.getMemberCount() + 1);
            clubRepository.save(club);
            user.getClubs().add(club);
            userRepository.save(user);
        }
    }

    @Override
    public void leaveClub(Long userId, Long clubId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new EntityNotFoundException("Club not found with id: " + clubId));

        if (club.getMembers().contains(user)) {
            club.getMembers().remove(user);
            club.setMemberCount(club.getMemberCount() - 1);
            clubRepository.save(club);
            user.getClubs().remove(club);
            userRepository.save(user);
        }
    }
}
