package suprun.anna.socialnetwork.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String CONSTRAINT_MARKER = "constraint";
    private static final String DUPLICATE_ENTRY_MESSAGE = "Duplicate entry";
    private static final String ERRORS = "errors";
    private static final String MESSAGE = "message";
    private static final String STATUS = "status";
    private static final String TABLE_FIELD_SEPARATOR = ".";
    private static final String TIMESTAMP = "timestamp";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP, LocalDateTime.now());
        body.put(STATUS, HttpStatus.BAD_REQUEST);
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(this::getErrorMessage)
                .toList();
        body.put(ERRORS, errors);
        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolation(
            DataIntegrityViolationException ex) {
        String errorMessage = ex.getMessage();
        if (errorMessage.contains(DUPLICATE_ENTRY_MESSAGE)) {
            errorMessage = "Field '" + extractFieldNameFromErrorMessage(errorMessage)
                    + "' must be unique.";
        }
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP, LocalDateTime.now());
        body.put(STATUS, HttpStatus.CONFLICT);
        body.put(ERRORS, "Data Integrity Violation");
        body.put(MESSAGE, errorMessage);
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    private String extractFieldNameFromErrorMessage(String errorMessage) {
        int startIndex = errorMessage.indexOf(TABLE_FIELD_SEPARATOR,
                errorMessage.indexOf(CONSTRAINT_MARKER)) + 1;
        int endIndex = errorMessage.length() - 1;
        return errorMessage.substring(startIndex, endIndex);
    }

    private String getErrorMessage(ObjectError e) {
        if (e instanceof FieldError) {
            String field = ((FieldError) e).getField();
            String message = e.getDefaultMessage();
            return field + " " + message;
        }
        return e.getDefaultMessage();
    }
}
