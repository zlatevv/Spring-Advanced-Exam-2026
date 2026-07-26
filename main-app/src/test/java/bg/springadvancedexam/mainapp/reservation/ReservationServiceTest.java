package bg.springadvancedexam.mainapp.reservation;

import bg.springadvancedexam.mainapp.dto.reservation.ReservationCreateRequest;
import bg.springadvancedexam.mainapp.dto.reservation.ReservationResponse;
import bg.springadvancedexam.mainapp.exception.request.RequestNotApprovedException;
import bg.springadvancedexam.mainapp.model.entity.manuscript.Manuscript;
import bg.springadvancedexam.mainapp.model.entity.request.AccessRequest;
import bg.springadvancedexam.mainapp.model.entity.reservation.Reservation;
import bg.springadvancedexam.mainapp.model.entity.user.User;
import bg.springadvancedexam.mainapp.model.enums.RequestStatus;
import bg.springadvancedexam.mainapp.repository.request.AccessRequestRepository;
import bg.springadvancedexam.mainapp.repository.reservation.ReservationRepository;
import bg.springadvancedexam.mainapp.service.reservation.ReservationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private AccessRequestRepository accessRequestRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void createReservation_shouldCreate_whenRequestApproved() {
        UUID requesterId = UUID.randomUUID();
        UUID accessRequestId = UUID.randomUUID();
        User researcher = User.builder().id(requesterId).fullName("Rhea Researcher").build();
        Manuscript manuscript = Manuscript.builder().id(UUID.randomUUID()).title("Codex Aureus").build();
        AccessRequest accessRequest = AccessRequest.builder()
                .id(accessRequestId)
                .researcher(researcher)
                .manuscript(manuscript)
                .requestStatus(RequestStatus.APPROVED)
                .build();
        ReservationCreateRequest request = new ReservationCreateRequest(
                accessRequestId, LocalDate.now().plusDays(1), LocalTime.of(10, 0));

        when(accessRequestRepository.findById(accessRequestId)).thenReturn(Optional.of(accessRequest));
        when(reservationRepository.save(any(Reservation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ReservationResponse result = reservationService.createReservation(request, requesterId);

        assertThat(result.manuscriptTitle()).isEqualTo("Codex Aureus");
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void createReservation_shouldThrow_whenAccessRequestNotApproved() {
        UUID requesterId = UUID.randomUUID();
        UUID accessRequestId = UUID.randomUUID();
        User researcher = User.builder().id(requesterId).build();
        AccessRequest accessRequest = AccessRequest.builder()
                .id(accessRequestId)
                .researcher(researcher)
                .requestStatus(RequestStatus.PENDING)
                .build();
        ReservationCreateRequest request = new ReservationCreateRequest(
                accessRequestId, LocalDate.now().plusDays(1), LocalTime.of(10, 0));

        when(accessRequestRepository.findById(accessRequestId)).thenReturn(Optional.of(accessRequest));

        assertThrows(RequestNotApprovedException.class, () ->
                reservationService.createReservation(request, requesterId));
    }
}
