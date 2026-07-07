package bg.springadvancedexam.backend.repository;

import bg.springadvancedexam.backend.model.entity.Manuscript;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ManuscriptRepository extends JpaRepository<Manuscript, UUID> {
}
