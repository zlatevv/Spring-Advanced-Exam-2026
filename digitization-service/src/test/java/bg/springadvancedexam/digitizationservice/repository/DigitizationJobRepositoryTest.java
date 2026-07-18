package bg.springadvancedexam.digitizationservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import bg.springadvancedexam.digitizationservice.model.entity.DigitizationJob;
import bg.springadvancedexam.digitizationservice.model.enums.JobStatus;
import bg.springadvancedexam.digitizationservice.model.enums.Priority;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
class DigitizationJobRepositoryTest {

    @Autowired
    private DigitizationJobRepository digitizationJobRepository;

    @Test
    void findByStatusNot_ShouldReturnJobsWithDifferentStatus() {

        DigitizationJob pendingJob = new DigitizationJob();
        pendingJob.setManuscriptId(UUID.randomUUID());
        pendingJob.setStatus(JobStatus.QUEUED);
        pendingJob.setPriority(Priority.HIGH);

        DigitizationJob completedJob = new DigitizationJob();
        completedJob.setManuscriptId(UUID.randomUUID());
        completedJob.setStatus(JobStatus.COMPLETE);
        completedJob.setPriority(Priority.LOW);

        digitizationJobRepository.save(pendingJob);
        digitizationJobRepository.save(completedJob);


        List<DigitizationJob> result =
                digitizationJobRepository.findByStatusNot(JobStatus.COMPLETE);


        assertThat(result)
                .hasSize(1)
                .contains(pendingJob);

        assertThat(result.getFirst().getStatus())
                .isEqualTo(JobStatus.QUEUED);
    }
}