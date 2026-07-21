package bg.springadvancedexam.mainapp.dto.manuscript;

import bg.springadvancedexam.mainapp.model.enums.ConservationStatus;
import bg.springadvancedexam.mainapp.model.enums.Era;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateManuscriptRequest(
        @NotNull(message = "Title is required")
        @Size(min = 3, message = "Title must be at least 3 characters")
        String title,
        @NotNull(message = "Description is required")
        String description,
        String author,
        Era era,
        String originRegion,
        String status,
        ConservationStatus conservationStatus
) {
}
