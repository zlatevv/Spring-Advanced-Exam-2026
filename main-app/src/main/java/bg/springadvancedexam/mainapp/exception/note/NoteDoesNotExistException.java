package bg.springadvancedexam.mainapp.exception.note;

public class NoteDoesNotExistException extends RuntimeException {
    public NoteDoesNotExistException(String message) {
        super(message);
    }
}
