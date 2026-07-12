package bg.springadvancedexam.mainapp.exception.reservation;

public class ReservationAccessException extends RuntimeException {
    public ReservationAccessException(String message) {
        super(message);
    }
}
