package bg.springadvancedexam.digitizationservice.component;

import bg.springadvancedexam.digitizationservice.model.entity.DigitizationJob;
import bg.springadvancedexam.digitizationservice.model.enums.JobStatus;
import bg.springadvancedexam.digitizationservice.repository.DigitizationJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JobProgressionScheduler {

    private final DigitizationJobRepository jobRepository;

    @Scheduled(fixedDelay = 120000)
    public void advanceJobs() {
        List<DigitizationJob> activeJobs = jobRepository.findByStatusNot(JobStatus.COMPLETE);

        for (DigitizationJob job : activeJobs) {
            switch (job.getStatus()) {
                case QUEUED -> job.setStatus(JobStatus.SCANNING);
                case SCANNING -> job.setStatus(JobStatus.RESTORING);
                case RESTORING -> {
                    job.setStatus(JobStatus.COMPLETE);
                    job.setCompletedAt(LocalDateTime.now());
                }
                default -> {
                }
            }
            jobRepository.save(job);
        }
    }
}
