package suprun.anna.socialnetwork.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;

@FieldMatch(first = "password", second = "repeatPassword", message = "The passwords must match")
public record UserRegistrationRequestDto(
        @NotBlank
        String email,
        @NotBlank
        @Length(min = 8, max = 20)
        String password,
        @NotBlank
        @Length(min = 8, max = 20)
        String repeatPassword,
        @NotBlank
        @Username
        String username,
        @Positive
        Integer age
) {
}

