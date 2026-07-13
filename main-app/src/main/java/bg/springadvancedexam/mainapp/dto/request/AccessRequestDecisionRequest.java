package bg.springadvancedexam.mainapp.dto.request;

import bg.springadvancedexam.mainapp.model.enums.RequestStatus;
import jakarta.validation.constraints.NotNull;

public record AccessRequestDecisionRequest(
        @NotNull(message = "Decision is required")
        RequestStatus decision
) {}
