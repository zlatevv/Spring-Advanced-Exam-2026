package bg.springadvancedexam.backend.repository;

import bg.springadvancedexam.backend.model.entity.ReadingRoomReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReadingRoomReservationRepository extends JpaRepository<ReadingRoomReservation, UUID> {
}
