package bg.springadvancedexam.mainapp.event;

import java.util.UUID;

public record AccessRequestApprovedEvent(
        UUID accessRequestId,
        UUID researcherId,
        UUID manuscriptId) {
}
