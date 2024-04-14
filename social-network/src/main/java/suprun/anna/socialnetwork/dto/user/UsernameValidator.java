package suprun.anna.socialnetwork.dto.user;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UsernameValidator implements ConstraintValidator<Username, String> {
    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9]+(?:[_\\-.][a-zA-Z0-9]+)*$";

    @Override
    public void initialize(Username constraintAnnotation) {
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username == null || username.length() < 3) {
            return false;
        }
        Pattern pattern = Pattern.compile(USERNAME_PATTERN);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }
}

