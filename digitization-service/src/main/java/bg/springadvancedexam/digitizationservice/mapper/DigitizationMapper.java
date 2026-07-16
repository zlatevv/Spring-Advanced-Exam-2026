package bg.springadvancedexam.digitizationservice.mapper;

import bg.springadvancedexam.digitizationservice.dto.DigitizationJobResponse;
import bg.springadvancedexam.digitizationservice.model.entity.DigitizationJob;
import bg.springadvancedexam.digitizationservice.model.enums.JobStatus;
import bg.springadvancedexam.digitizationservice.model.enums.Priority;

import java.time.LocalDateTime;
import java.util.UUID;

public class DigitizationMapper {
    public static DigitizationJobResponse toDigitizationJobResponse(DigitizationJob digitizationJob) {
        return new DigitizationJobResponse(
                digitizationJob.getManuscriptId(),
                digitizationJob.getStatus(),
                digitizationJob.getPriority(),
                digitizationJob.getTechnician(),
                digitizationJob.getRequestedAt(),
                digitizationJob.getCompletedAt()
        );
    }

    public static DigitizationJob toDigitizationJob(UUID manuscriptId, Priority priority) {
        return DigitizationJob.builder()
                .manuscriptId(manuscriptId)
                .priority(priority)
                .status(JobStatus.QUEUED)
                .requestedAt(LocalDateTime.now())
                .build();
    }
}
