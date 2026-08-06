package bg.springadvancedexam.mainapp.exception.reservation;

public class InvalidReservationTimeException extends RuntimeException {
    public InvalidReservationTimeException(String message) {
        super(message);
    }
}
