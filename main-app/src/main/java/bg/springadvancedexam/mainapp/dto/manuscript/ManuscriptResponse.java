package bg.springadvancedexam.mainapp.dto.manuscript;

import bg.springadvancedexam.mainapp.model.enums.ConservationStatus;
import bg.springadvancedexam.mainapp.model.enums.DigitizationStatus;
import bg.springadvancedexam.mainapp.model.enums.Era;
import bg.springadvancedexam.mainapp.model.enums.Visibility;

import java.util.UUID;

public record ManuscriptResponse(
        UUID id,
        String title,
        String author,
        Era era,
        String originRegion,
        String description,
        ConservationStatus conservationStatus,
        Visibility visibility,
        DigitizationStatus digitizationStatus
) {
}
