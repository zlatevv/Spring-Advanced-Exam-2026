package bg.springadvancedexam.mainapp.repository.reservation;

import bg.springadvancedexam.mainapp.model.entity.reservation.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    List<Reservation> findByAccessRequest_Researcher_Id(UUID researcherId);
}
