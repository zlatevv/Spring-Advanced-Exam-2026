package bg.springadvancedexam.backend.repository;

import bg.springadvancedexam.backend.model.entity.StudyNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StudyNoteRepository extends JpaRepository<StudyNote, UUID> {
}
