package bg.springadvancedexam.mainapp.exception.request;

public class InvalidRequestDecisionException extends RuntimeException {
    public InvalidRequestDecisionException(String message) {
        super(message);
    }
}
