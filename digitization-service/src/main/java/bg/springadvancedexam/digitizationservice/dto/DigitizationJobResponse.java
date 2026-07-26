package bg.springadvancedexam.digitizationservice.dto;

import bg.springadvancedexam.digitizationservice.model.enums.JobStatus;
import bg.springadvancedexam.digitizationservice.model.enums.Priority;

import java.time.LocalDateTime;
import java.util.UUID;


public record DigitizationJobResponse(
        UUID manuscriptId,
        JobStatus status,
        Priority priority,
        String technician,
        LocalDateTime requestedAt,
        LocalDateTime completedAt
) {
}
