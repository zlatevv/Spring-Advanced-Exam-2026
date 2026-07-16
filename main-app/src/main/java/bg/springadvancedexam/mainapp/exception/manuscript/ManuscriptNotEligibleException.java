package bg.springadvancedexam.mainapp.exception.manuscript;

public class ManuscriptNotEligibleException extends RuntimeException {
    public ManuscriptNotEligibleException(String message) {
        super(message);
    }
}
