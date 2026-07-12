package bg.springadvancedexam.mainapp.model.entity.reservation;

import bg.springadvancedexam.mainapp.model.entity.request.AccessRequest;
import bg.springadvancedexam.mainapp.model.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "reading_room_reservations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "access_request_id")
    private AccessRequest accessRequest;

    private LocalDate slotDate;

    private LocalTime slotTime;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;
}
