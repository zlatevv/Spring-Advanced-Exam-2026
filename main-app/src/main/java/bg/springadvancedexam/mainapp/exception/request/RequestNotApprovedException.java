package bg.springadvancedexam.mainapp.exception.request;

public class RequestNotApprovedException extends RuntimeException {
    public RequestNotApprovedException(String message) {
        super(message);
    }
}
