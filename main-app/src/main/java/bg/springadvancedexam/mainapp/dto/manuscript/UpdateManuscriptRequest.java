package bg.springadvancedexam.mainapp.dto.manuscript;

import bg.springadvancedexam.mainapp.model.enums.ConservationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateManuscriptRequest(
        @NotBlank(message = "Author is required")
        String author,
        @NotBlank(message = "Description is required")
        String description,
        @NotNull(message = "Conservation status is required")
        ConservationStatus conservationStatus
) {}
