package bg.springadvancedexam.digitizationservice.repository;

import bg.springadvancedexam.digitizationservice.model.entity.DigitizationJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DigitizationJobRepository extends JpaRepository<DigitizationJob, UUID> {
}
