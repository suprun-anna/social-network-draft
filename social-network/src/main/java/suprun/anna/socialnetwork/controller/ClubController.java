package suprun.anna.socialnetwork.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import suprun.anna.socialnetwork.dto.club.ClubCreateDto;
import suprun.anna.socialnetwork.dto.club.ClubDto;
import suprun.anna.socialnetwork.dto.club.ClubRedirectResponseDto;
import suprun.anna.socialnetwork.dto.club.ClubUpdateDto;
import suprun.anna.socialnetwork.dto.user.UserRedirectResponseDto;
import suprun.anna.socialnetwork.model.User;
import suprun.anna.socialnetwork.service.club.ClubService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/club")
public class ClubController {
    private final ClubService clubService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ClubDto createClub(Authentication authentication, @RequestBody ClubCreateDto clubCreateDto) {
        User user = (User) authentication.getPrincipal();
        return clubService.save(user, clubCreateDto);
    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ClubDto updateClub(Authentication authentication, @RequestBody ClubUpdateDto clubUpdateDto) {
        User user = (User) authentication.getPrincipal();
        return clubService.update(user, clubUpdateDto);
    }

    @DeleteMapping("/{clubId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void deleteClub(Authentication authentication, @RequestParam Long clubId){
        User user = (User) authentication.getPrincipal();
        clubService.delete(user, clubId);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<ClubRedirectResponseDto> findOpenClubsByPartialName(@RequestParam String name, Pageable pageable) {
        return clubService.findOpenClubsByPartialName(name, pageable);
    }

    @GetMapping("byId")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ClubDto findClub(Authentication authentication, @RequestParam Long id) {
        User user = (User) authentication.getPrincipal();
        return clubService.findClubById(user.getId(), id);
    }

    @GetMapping("isMember")
    @PreAuthorize("hasRole('ROLE_USER')")
    public boolean findClubMember(Authentication authentication, @RequestParam Long id) {
        User user = (User) authentication.getPrincipal();
        return clubService.checkClubMemberExistence(user.getId(), id);
    }

//    @GetMapping("/my")
//    @PreAuthorize("hasRole('ROLE_USER')")
//    public List<ClubDto> findAllUserClubs(Authentication authentication, Pageable pageable) {
//        User user = (User) authentication.getPrincipal();
//        return clubService.findUserClubs(user.getId(), pageable);
//    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<ClubDto> findAllUserClubsByName(Authentication authentication, @RequestParam String name, Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        System.out.println("search for my clubs");
        return name.isBlank() ? clubService.findUserClubs(user.getId(), pageable) :
                clubService.findUserClubsByName(user.getId(), name, pageable);
    }

    @GetMapping("/myOwn")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<ClubRedirectResponseDto> searchOwnedClubs(Authentication authentication, @RequestParam String name, Pageable pageable){
        System.out.println("search for owned clubs");
        User user = (User) authentication.getPrincipal();
        return clubService.findOwnedClubsPartialName(user.getId(), name, pageable);
    }

    @GetMapping("/members")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<UserRedirectResponseDto> findAllClubMembers(@RequestParam Long clubId, Pageable pageable) {
        return clubService.findClubMembers(clubId, pageable);
    }

    @PostMapping("/join")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> joinClub(Authentication authentication, @RequestParam Long clubId) {
        User user = (User) authentication.getPrincipal();
        clubService.joinClub(user.getId(), clubId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/leave")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> leaveClub(Authentication authentication, @RequestParam Long clubId) {
        User user = (User) authentication.getPrincipal();
        clubService.leaveClub(user.getId(), clubId);
        return ResponseEntity.ok().build();
    }
}
