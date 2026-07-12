package bg.springadvancedexam.mainapp.exception.request;

public class RequestNotPermittedException extends RuntimeException {
    public RequestNotPermittedException(String message) {
        super(message);
    }
}
