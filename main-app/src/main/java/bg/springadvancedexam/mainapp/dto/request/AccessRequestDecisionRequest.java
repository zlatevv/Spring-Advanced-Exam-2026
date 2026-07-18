package bg.springadvancedexam.mainapp.dto.request;

import bg.springadvancedexam.mainapp.model.enums.RequestStatus;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AccessRequestDecisionRequest(
        UUID requestId,
        @NotNull(message = "Decision is required")
        RequestStatus decision
) {}
