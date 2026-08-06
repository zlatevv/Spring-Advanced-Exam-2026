package bg.springadvancedexam.mainapp.service.reservation;

import bg.springadvancedexam.mainapp.dto.reservation.ReservationCreateRequest;
import bg.springadvancedexam.mainapp.dto.reservation.ReservationResponse;
import bg.springadvancedexam.mainapp.exception.request.RequestNotApprovedException;
import bg.springadvancedexam.mainapp.exception.request.RequestNotFoundException;
import bg.springadvancedexam.mainapp.exception.reservation.InvalidReservationTimeException;
import bg.springadvancedexam.mainapp.exception.reservation.ReservationAccessException;
import bg.springadvancedexam.mainapp.exception.reservation.ReservationNotFoundException;
import bg.springadvancedexam.mainapp.mapper.reservation.ReservationMapper;
import bg.springadvancedexam.mainapp.model.entity.request.AccessRequest;
import bg.springadvancedexam.mainapp.model.entity.reservation.Reservation;
import bg.springadvancedexam.mainapp.model.enums.RequestStatus;
import bg.springadvancedexam.mainapp.model.enums.ReservationStatus;
import bg.springadvancedexam.mainapp.model.enums.Role;
import bg.springadvancedexam.mainapp.repository.request.AccessRequestRepository;
import bg.springadvancedexam.mainapp.repository.reservation.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private static final ZoneId RESERVATION_ZONE = ZoneId.of("Europe/Sofia");

    private final ReservationRepository reservationRepository;
    private final AccessRequestRepository accessRequestRepository;

    @Transactional
    public ReservationResponse createReservation(ReservationCreateRequest request, UUID requesterId) {
        LocalDateTime reservationDateTime = LocalDateTime.of(
                request.slotDate(),
                request.slotTime()
        );

        LocalDateTime now = LocalDateTime.now(RESERVATION_ZONE);

        if (!reservationDateTime.isAfter(now)) {
            throw new InvalidReservationTimeException("Reservation time must be in the future.");
        }

        AccessRequest accessRequest = accessRequestRepository.findById(request.accessRequestId())
                .orElseThrow(() -> new RequestNotFoundException("Access request not found!"));

        if (!accessRequest.getResearcher().getId().equals(requesterId)) {
            throw new ReservationAccessException("This is not your access request.");
        }

        if (accessRequest.getRequestStatus() != RequestStatus.APPROVED) {
            throw new RequestNotApprovedException("Access request has not been approved.");
        }

        Reservation reservation = ReservationMapper.toReservation(accessRequest, request);
        Reservation saved = reservationRepository.save(reservation);

        return ReservationMapper.toReservationResponse(saved);
    }

    public List<ReservationResponse> fetchMyReservations(UUID researcherId) {
        return reservationRepository
                .findByAccessRequest_Researcher_Id(researcherId)
                .stream()
                .map(ReservationMapper::toReservationResponse)
                .toList();
    }

    @Transactional
    public void cancelReservation(UUID reservationId, UUID requesterId, Role requesterRole) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found!"));

        boolean isOwner = reservation.getAccessRequest().getResearcher().getId().equals(requesterId);
        boolean isAdmin = requesterRole == Role.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new ReservationAccessException("You are not the owner or an admin to cancel this reservation!");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);

        AccessRequest accessRequest = reservation.getAccessRequest();
        accessRequest.setRequestStatus(RequestStatus.WITHDRAWN);
        accessRequestRepository.save(accessRequest);
    }

    public List<ReservationResponse> fetchReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationMapper::toReservationResponse)
                .toList();
    }
}
