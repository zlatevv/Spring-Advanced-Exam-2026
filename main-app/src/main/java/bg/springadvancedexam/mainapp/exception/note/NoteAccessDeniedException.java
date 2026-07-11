package bg.springadvancedexam.mainapp.exception.note;

public class NoteAccessDeniedException extends RuntimeException {
    public NoteAccessDeniedException(String message) {
        super(message);
    }
}
