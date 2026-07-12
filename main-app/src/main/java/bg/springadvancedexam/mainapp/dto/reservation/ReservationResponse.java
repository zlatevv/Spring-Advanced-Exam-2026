package bg.springadvancedexam.mainapp.dto.reservation;

import bg.springadvancedexam.mainapp.model.enums.ReservationStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record ReservationResponse(
        UUID manuscriptId,
        String manuscriptTitle,
        String researcherName,
        LocalDate slotDate,
        LocalTime slotTime,
        ReservationStatus status
) {}
