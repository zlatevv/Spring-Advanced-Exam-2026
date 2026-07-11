package bg.springadvancedexam.mainapp.repository.reservation;

import bg.springadvancedexam.mainapp.model.entity.reservation.ReadingRoomReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReadingRoomReservationRepository extends JpaRepository<ReadingRoomReservation, UUID> {
}
