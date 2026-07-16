package bg.springadvancedexam.digitizationservice.exception;

public class DigitizationJobNotFoundException extends RuntimeException {
    public DigitizationJobNotFoundException(String message) {
        super(message);
    }
}
