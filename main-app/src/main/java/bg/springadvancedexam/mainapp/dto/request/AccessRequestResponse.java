package bg.springadvancedexam.mainapp.dto.request;

import bg.springadvancedexam.mainapp.model.enums.RequestStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record AccessRequestResponse(
        UUID manuscriptId,
        String manuscriptTitle,
        String researcherName,
        String purpose,
        RequestStatus status,
        LocalDateTime requestedDate
) {}