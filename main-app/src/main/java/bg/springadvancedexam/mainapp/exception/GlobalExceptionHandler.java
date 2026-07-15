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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // --- Built-in exception: Bean Validation failures (@Valid on request DTOs) ---
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<String> messages = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(String.join("; ", messages)));
    }

    // --- Built-in exception: Spring Security login failure ---
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Invalid email or password."));
    }

    // --- Custom exceptions: "not found" family → 404 ---
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

    // --- Custom exceptions: "access denied" family → 403 ---
    @ExceptionHandler({
            NoteAccessDeniedException.class,
            ReservationAccessException.class,
            ManuscriptAccessDeniedException.class
    })
    public ResponseEntity<ErrorResponse> handleAccessDenied(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(ex.getMessage()));
    }

    // --- Custom exceptions: "conflict with current state" family → 409 ---
    @ExceptionHandler({
            UserAlreadyExistsException.class,
            EmailExistsException.class,
            LastAdminException.class,
            RequestAlreadyDecidedException.class
    })
    public ResponseEntity<ErrorResponse> handleConflict(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(ex.getMessage()));
    }

    // --- Custom exceptions: malformed business input → 400 ---
    @ExceptionHandler(InvalidRequestDecisionException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDecision(InvalidRequestDecisionException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
    }

    // --- Fallback: anything unexpected → 500, never leak the raw exception ---
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("An unexpected error occurred."));
    }
}
