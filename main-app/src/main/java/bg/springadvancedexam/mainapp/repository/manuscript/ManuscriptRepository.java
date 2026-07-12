package bg.springadvancedexam.mainapp.repository.manuscript;

import bg.springadvancedexam.mainapp.model.entity.manuscript.Manuscript;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ManuscriptRepository extends JpaRepository<Manuscript, UUID> {
    Optional<Manuscript> findByTitle(String s);
}
