package bg.springadvancedexam.mainapp.mapper.manuscript;

import bg.springadvancedexam.mainapp.dto.manuscript.CreateManuscriptRequest;
import bg.springadvancedexam.mainapp.dto.manuscript.ManuscriptResponse;
import bg.springadvancedexam.mainapp.model.entity.manuscript.Manuscript;
import bg.springadvancedexam.mainapp.model.enums.DigitizationStatus;
import bg.springadvancedexam.mainapp.model.enums.Visibility;

import java.time.LocalDateTime;

public class ManuscriptMapper {
    public static Manuscript toManuscriptEntity(CreateManuscriptRequest createManuscriptRequest) {
        return Manuscript.builder()
                .era(createManuscriptRequest.era())
                .author(createManuscriptRequest.author())
                .title(createManuscriptRequest.title())
                .createdAt(LocalDateTime.now())
                .description(createManuscriptRequest.description())
                .digitizationStatus(DigitizationStatus.NOT_STARTED)
                .visibility(Visibility.RESTRICTED)
                .conservationStatus(createManuscriptRequest.conservationStatus())
                .originRegion(createManuscriptRequest.originRegion())
                .build();
    }

    public static ManuscriptResponse toManuscriptResponse(Manuscript manuscript) {
        return new ManuscriptResponse(
                manuscript.getId(),
                manuscript.getTitle(),
                manuscript.getAuthor(),
                manuscript.getEra(),
                manuscript.getOriginRegion(),
                manuscript.getDescription(),
                manuscript.getConservationStatus(),
                manuscript.getVisibility(),
                manuscript.getDigitizationStatus()
        );
    }
}
