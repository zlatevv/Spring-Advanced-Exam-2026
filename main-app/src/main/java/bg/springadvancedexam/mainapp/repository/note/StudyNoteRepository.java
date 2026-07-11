package bg.springadvancedexam.mainapp.repository.note;

import bg.springadvancedexam.mainapp.model.entity.note.StudyNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StudyNoteRepository extends JpaRepository<StudyNote, UUID> {
    List<StudyNote> findAllByManuscriptId(UUID manuscriptId);
}
