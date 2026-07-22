package bg.springadvancedexam.mainapp.exception;

import bg.springadvancedexam.mainapp.exception.manuscript.ManuscriptAccessDeniedException;
import bg.springadvancedexam.mainapp.exception.manuscript.ManuscriptDoesNotExistException;
import bg.springadvancedexam.mainapp.exception.note.NoteAccessDeniedException;
import bg.springadvancedexam.mainapp.exception.note.NoteDoesNotExistException;
import bg.springadvancedexam.mainapp.exception.request.InvalidRequestDecisionException;
import bg.springadvancedexam.mainapp.exception.request.RequestAlreadyDecidedException;
import bg.springadvancedexam.mainapp.exception.request.RequestNotFoundException;
import bg.springadvancedexam.mainapp.exception.reservation.ReservationAccessException;
import bg.springadvancedexam.mainapp.exception.reservation.ReservationNotFoundException;
import bg.springadvancedexam.mainapp.exception.user.EmailExistsException;
import bg.springadvancedexam.mainapp.exception.user.LastAdminException;
import bg.springadvancedexam.mainapp.exception.user.UserAlreadyExistsException;
import bg.springadvancedexam.mainapp.exception.user.UserNotFoundException;
import com.openai.errors.RateLimitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<String> messages = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(String.join("; ", messages)));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Invalid email or password."));
    }

    @ExceptionHandler({
            UserNotFoundException.class,
            ManuscriptDoesNotExistException.class,
            NoteDoesNotExistException.class,
            ReservationNotFoundException.class,
            RequestNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler({
            NoteAccessDeniedException.class,
            ReservationAccessException.class,
            ManuscriptAccessDeniedException.class
    })
    public ResponseEntity<ErrorResponse> handleAccessDenied(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler({
            UserAlreadyExistsException.class,
            EmailExistsException.class,
            LastAdminException.class,
            RequestAlreadyDecidedException.class
    })
    public ResponseEntity<ErrorResponse> handleConflict(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(InvalidRequestDecisionException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDecision(InvalidRequestDecisionException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex) {
        log.error("Unexpected error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("An unexpected error occurred."));
    }

    @ExceptionHandler(RateLimitException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public Map<String, String> handleRateLimit(RateLimitException e) {
        return Map.of(
                "error",
                "OpenAI quota exceeded. Check billing."
        );
    }
}
