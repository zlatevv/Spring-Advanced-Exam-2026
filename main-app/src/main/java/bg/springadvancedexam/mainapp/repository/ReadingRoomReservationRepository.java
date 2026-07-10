package bg.springadvancedexam.mainapp.repository;

import bg.springadvancedexam.mainapp.model.entity.ReadingRoomReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReadingRoomReservationRepository extends JpaRepository<ReadingRoomReservation, UUID> {
}
