package bg.springadvancedexam.mainapp.repository;

import bg.springadvancedexam.mainapp.model.entity.Manuscript;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ManuscriptRepository extends JpaRepository<Manuscript, UUID> {
}
