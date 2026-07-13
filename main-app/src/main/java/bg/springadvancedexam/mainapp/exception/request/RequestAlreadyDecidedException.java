package bg.springadvancedexam.mainapp.exception.request;

public class RequestAlreadyDecidedException extends RuntimeException {
    public RequestAlreadyDecidedException(String message) {
        super(message);
    }
}
