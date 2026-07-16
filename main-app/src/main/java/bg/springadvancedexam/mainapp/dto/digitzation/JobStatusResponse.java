package bg.springadvancedexam.mainapp.dto.digitzation;


import bg.springadvancedexam.mainapp.model.enums.JobStatus;
import bg.springadvancedexam.mainapp.model.enums.Priority;

import java.time.LocalDateTime;
import java.util.UUID;

public record JobStatusResponse(
        UUID manuscriptId,
        JobStatus status,
        Priority priority,
        String technician,
        LocalDateTime requestedAt,
        LocalDateTime completedAt
) {}

