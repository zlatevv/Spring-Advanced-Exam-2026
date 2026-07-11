package bg.springadvancedexam.mainapp.exception.manuscript;

public class ManuscriptDoesNotExistException extends RuntimeException {
    public ManuscriptDoesNotExistException(String message) {
        super(message);
    }
}
