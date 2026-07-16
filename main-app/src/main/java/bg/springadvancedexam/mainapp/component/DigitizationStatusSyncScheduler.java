package bg.springadvancedexam.mainapp.component;

import bg.springadvancedexam.mainapp.client.DigitizationClient;
import bg.springadvancedexam.mainapp.model.entity.manuscript.Manuscript;
import bg.springadvancedexam.mainapp.model.enums.DigitizationStatus;
import bg.springadvancedexam.mainapp.model.enums.JobStatus;
import bg.springadvancedexam.mainapp.repository.manuscript.ManuscriptRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DigitizationStatusSyncScheduler {

    private final ManuscriptRepository manuscriptRepository;
    private final DigitizationClient digitizationClient;

    @Scheduled(cron = "0 */2 * * * *")
    public void syncDigitizationStatuses() {
        List<Manuscript> queued = manuscriptRepository.findByDigitizationStatus(DigitizationStatus.QUEUED);

        for (Manuscript manuscript : queued) {
            try {
                JobStatus status = digitizationClient.getStatus(manuscript.getId()).status();
                if (status == JobStatus.COMPLETE) {
                    manuscript.setDigitizationStatus(DigitizationStatus.COMPLETE);
                    manuscriptRepository.save(manuscript);
                    log.info("Manuscript {} marked digitization COMPLETE", manuscript.getId());
                }
            } catch (Exception e) {
                log.warn("Could not sync digitization status for manuscript {}: {}",
                        manuscript.getId(), e.getMessage());
            }
        }
    }
}
