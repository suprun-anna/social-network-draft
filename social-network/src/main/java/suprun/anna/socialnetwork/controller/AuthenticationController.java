package suprun.anna.socialnetwork.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import suprun.anna.socialnetwork.dto.user.UserLoginRequestDto;
import suprun.anna.socialnetwork.dto.user.UserLoginResponseDto;
import suprun.anna.socialnetwork.dto.user.UserRegistrationRequestDto;
import suprun.anna.socialnetwork.dto.user.UserResponseDto;
import suprun.anna.socialnetwork.exception.RegistrationException;
import suprun.anna.socialnetwork.service.user.AuthenticationService;
import suprun.anna.socialnetwork.service.user.UserService;
import org.springframework.http.HttpStatus;

@Tag(name = "User manager", description = "Endpoints for user authentication")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/auth")
//@CrossOrigin(origins = "http://localhost:8081")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Operation(summary = "Register a new user", description = "Register a new user")
    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto request)
            throws RegistrationException {
        System.out.println("Registration");
        return userService.register(request);
    }

    @Operation(summary = "Login", description = "Login")
    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody UserLoginRequestDto request) {
        System.out.println("Login");
        return authenticationService.authenticate(request);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}



