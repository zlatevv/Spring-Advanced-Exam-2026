package bg.springadvancedexam.mainapp.dto.manuscript;

import bg.springadvancedexam.mainapp.model.enums.Visibility;
import jakarta.validation.constraints.NotNull;

public record VisibilityRequest(
        @NotNull(message = "Visibility is required")
        Visibility visibility
) {
}
