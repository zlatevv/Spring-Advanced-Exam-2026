package bg.springadvancedexam.mainapp.repository;

import bg.springadvancedexam.mainapp.model.entity.StudyNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StudyNoteRepository extends JpaRepository<StudyNote, UUID> {
}
