package bg.springadvancedexam.mainapp.dto.reservation;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record ReservationCreateRequest(
        @NotNull(message = "Access request ID is required")
        UUID accessRequestId,

        @NotNull(message = "Slot date is required")
        @FutureOrPresent(message = "Slot date must be today or in the future")
        LocalDate slotDate,

        @NotNull(message = "Slot time is required")
        LocalTime slotTime
) {}
