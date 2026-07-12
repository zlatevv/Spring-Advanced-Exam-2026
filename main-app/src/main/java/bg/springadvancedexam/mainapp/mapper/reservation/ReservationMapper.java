package bg.springadvancedexam.mainapp.mapper.reservation;

import bg.springadvancedexam.mainapp.dto.reservation.ReservationResponse;
import bg.springadvancedexam.mainapp.dto.reservation.ReservationCreateRequest;
import bg.springadvancedexam.mainapp.model.entity.request.AccessRequest;
import bg.springadvancedexam.mainapp.model.entity.reservation.Reservation;
import bg.springadvancedexam.mainapp.model.enums.ReservationStatus;

public class ReservationMapper {

    public static Reservation toReservation(AccessRequest accessRequest, ReservationCreateRequest request) {
        Reservation reservation = new Reservation();
        reservation.setAccessRequest(accessRequest);
        reservation.setSlotDate(request.slotDate());
        reservation.setSlotTime(request.slotTime());
        reservation.setStatus(ReservationStatus.CONFIRMED);
        return reservation;
    }

    public static ReservationResponse toReservationResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.getAccessRequest().getManuscript().getId(),
                reservation.getAccessRequest().getManuscript().getTitle(),
                reservation.getAccessRequest().getResearcher().getFullName(),
                reservation.getSlotDate(),
                reservation.getSlotTime(),
                reservation.getStatus()
        );
    }
}
