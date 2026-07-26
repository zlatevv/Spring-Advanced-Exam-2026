package bg.springadvancedexam.mainapp.mapper.reservation;

import bg.springadvancedexam.mainapp.dto.reservation.ReservationCreateRequest;
import bg.springadvancedexam.mainapp.dto.reservation.ReservationResponse;
import bg.springadvancedexam.mainapp.model.entity.request.AccessRequest;
import bg.springadvancedexam.mainapp.model.entity.reservation.Reservation;
import bg.springadvancedexam.mainapp.model.enums.ReservationStatus;

public class ReservationMapper {

    public static Reservation toReservation(AccessRequest accessRequest, ReservationCreateRequest request) {
        return Reservation.builder()
                .accessRequest(accessRequest)
                .slotDate(request.slotDate())
                .slotTime(request.slotTime())
                .status(ReservationStatus.CONFIRMED)
                .build();
    }

    public static ReservationResponse toReservationResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getAccessRequest().getManuscript().getId(),
                reservation.getAccessRequest().getManuscript().getTitle(),
                reservation.getAccessRequest().getResearcher().getFullName(),
                reservation.getSlotDate(),
                reservation.getSlotTime(),
                reservation.getStatus()
        );
    }
}
