package bg.springadvancedexam.mainapp.exception.manuscript;

public class ManuscriptAccessDeniedException extends RuntimeException {
    public ManuscriptAccessDeniedException(String message) {
        super(message);
    }
}
