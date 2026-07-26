package bg.springadvancedexam.mainapp.dto.digitzation;

import bg.springadvancedexam.mainapp.model.enums.Priority;

import java.util.UUID;

public record CreateJobRequest(
        UUID manuscriptId,
        Priority priority
) {
}