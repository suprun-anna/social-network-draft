package suprun.anna.socialnetwork.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import suprun.anna.socialnetwork.dto.group.ClubCreateDto;
import suprun.anna.socialnetwork.dto.group.ClubDto;
import suprun.anna.socialnetwork.dto.group.ClubUpdateDto;
import suprun.anna.socialnetwork.model.User;
import suprun.anna.socialnetwork.service.group.ClubService;

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
    public void deleteClub(Authentication authentication, @PathVariable Long clubId){
        User user = (User) authentication.getPrincipal();
        clubService.delete(user, clubId);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<ClubDto> findClubsByPartialName(@RequestParam String name, Pageable pageable) {
        return clubService.findByPartialName(name, pageable);
    }
}
