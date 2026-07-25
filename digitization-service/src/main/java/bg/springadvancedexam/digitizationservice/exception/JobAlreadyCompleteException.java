package bg.springadvancedexam.digitizationservice.exception;

public class JobAlreadyCompleteException extends RuntimeException {
    public JobAlreadyCompleteException(String message) {
        super(message);
    }
}
