package bg.springadvancedexam.mainapp.exception.user;

public class EmailExistsException extends RuntimeException {
    public EmailExistsException(String message) {
        super(message);
    }
}
