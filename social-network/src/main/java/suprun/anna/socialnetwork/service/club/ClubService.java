package suprun.anna.socialnetwork.service.club;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import suprun.anna.socialnetwork.dto.club.ClubDto;
import suprun.anna.socialnetwork.dto.club.ClubCreateDto;
import suprun.anna.socialnetwork.dto.club.ClubRedirectResponseDto;
import suprun.anna.socialnetwork.dto.club.ClubUpdateDto;
import suprun.anna.socialnetwork.dto.user.UserRedirectResponseDto;
import suprun.anna.socialnetwork.dto.user.UserResponseDto;
import suprun.anna.socialnetwork.model.User;

import java.io.IOException;
import java.util.List;

public interface ClubService {
    ClubDto save(User user, ClubCreateDto clubCreateDto);

    ClubDto update(User user, ClubUpdateDto clubUpdateDto);

    void delete(User user, Long clubId);

    List<ClubRedirectResponseDto> findByPartialName(String name, Pageable pageable);

    List<ClubRedirectResponseDto> findOwnedClubsPartialName(Long userId, String name, Pageable pageable);

    List<ClubRedirectResponseDto> findOpenClubsByPartialName(String name, Pageable pageable);

    List<ClubDto> findUserClubs(Long userId, Pageable pageable);

    List<ClubDto> findUserClubsByName(Long userId, String name, Pageable pageable);

    ClubDto findClubById(Long userId, Long clubId);

    ClubDto findById(Long clubId);

    boolean checkClubMemberExistence(Long userId, Long clubId);

    List<UserRedirectResponseDto> findClubMembers(Long clubId, Pageable pageable);

    void joinClub(Long userId, Long clubId);

    void leaveClub(Long userId, Long clubId);

    ClubDto updateProfilePicture(User user, Long clubId, MultipartFile profilePicture) throws IOException;
}
