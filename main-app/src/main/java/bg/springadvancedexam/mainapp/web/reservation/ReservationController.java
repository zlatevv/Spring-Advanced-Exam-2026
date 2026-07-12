package bg.springadvancedexam.mainapp.web.reservation;

import bg.springadvancedexam.mainapp.dto.reservation.ReservationCreateRequest;
import bg.springadvancedexam.mainapp.dto.reservation.ReservationResponse;
import bg.springadvancedexam.mainapp.security.CustomUserDetails;
import bg.springadvancedexam.mainapp.service.reservation.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @Valid @RequestBody ReservationCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails principal) {
        ReservationResponse created = reservationService.createReservation(request, principal.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ReservationResponse>> getMyReservations(
            @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(reservationService.fetchMyReservations(principal.getUserId()));
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getAllReservations() {
        return ResponseEntity.ok(reservationService.fetchReservations());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails principal) {
        reservationService.cancelReservation(id, principal.getUserId(), principal.getRole());
        return ResponseEntity.noContent().build();
    }
}
