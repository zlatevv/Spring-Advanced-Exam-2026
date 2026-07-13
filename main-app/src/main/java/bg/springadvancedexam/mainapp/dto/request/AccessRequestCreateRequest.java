package bg.springadvancedexam.mainapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record AccessRequestCreateRequest(
        @NotNull(message = "Manuscript ID is required")
        UUID manuscriptId,

        @NotBlank(message = "Purpose is required")
        @Size(min = 10, message = "Purpose must be at least 10 characters")
        String purpose
) {}
