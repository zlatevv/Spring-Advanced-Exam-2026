package bg.springadvancedexam.digitizationservice.dto;

import bg.springadvancedexam.digitizationservice.model.enums.Priority;

import java.util.UUID;

public record CreateJobRequest(
        UUID manuscriptId,
        Priority priority
) {
}