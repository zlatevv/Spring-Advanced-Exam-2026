package bg.springadvancedexam.mainapp.exception.request;

public class RequestDoesNotExistException extends RuntimeException {
    public RequestDoesNotExistException(String message) {
        super(message);
    }
}
