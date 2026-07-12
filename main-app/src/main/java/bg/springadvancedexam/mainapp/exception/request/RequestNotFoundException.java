package bg.springadvancedexam.mainapp.exception.request;

public class RequestNotFoundException extends RuntimeException {
    public RequestNotFoundException(String message) {
        super(message);
    }
}
