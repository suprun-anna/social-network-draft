package suprun.anna.socialnetwork.service.user;

import suprun.anna.socialnetwork.dto.user.UserLoginRequestDto;
import suprun.anna.socialnetwork.dto.user.UserLoginResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {
    UserLoginResponseDto authenticate(UserLoginRequestDto userLoginRequestDto);
}
