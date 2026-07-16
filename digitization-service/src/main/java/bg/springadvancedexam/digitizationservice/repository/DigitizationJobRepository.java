package bg.springadvancedexam.digitizationservice.repository;

import bg.springadvancedexam.digitizationservice.model.entity.DigitizationJob;
import bg.springadvancedexam.digitizationservice.model.enums.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DigitizationJobRepository extends JpaRepository<DigitizationJob, UUID> {
    Optional<DigitizationJob> findByManuscriptId(UUID manuscriptId);

    List<DigitizationJob> findByStatusNot(JobStatus jobStatus);
}
