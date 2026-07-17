package bg.springadvancedexam.mainapp.note;

import bg.springadvancedexam.mainapp.dto.note.NoteAddRequest;
import bg.springadvancedexam.mainapp.dto.note.NoteResponse;
import bg.springadvancedexam.mainapp.exception.manuscript.ManuscriptDoesNotExistException;
import bg.springadvancedexam.mainapp.model.entity.manuscript.Manuscript;
import bg.springadvancedexam.mainapp.model.entity.note.StudyNote;
import bg.springadvancedexam.mainapp.model.entity.user.User;
import bg.springadvancedexam.mainapp.repository.manuscript.ManuscriptRepository;
import bg.springadvancedexam.mainapp.repository.note.StudyNoteRepository;
import bg.springadvancedexam.mainapp.repository.user.UserRepository;
import bg.springadvancedexam.mainapp.service.note.NoteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

    @Mock
    private StudyNoteRepository studyNoteRepository;
    @Mock
    private ManuscriptRepository manuscriptRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CacheManager cacheManager;

    @InjectMocks
    private NoteService noteService;

    @Test
    void addNote_shouldCreateNote_whenValid() {
        UUID manuscriptId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        Manuscript manuscript = Manuscript.builder().id(manuscriptId).title("Codex Aureus").build();
        User author = User.builder().id(authorId).fullName("Rhea Researcher").build();
        NoteAddRequest request = new NoteAddRequest("A note about the binding technique used here.");

        when(manuscriptRepository.findById(manuscriptId)).thenReturn(Optional.of(manuscript));
        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(studyNoteRepository.save(any(StudyNote.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        NoteResponse result = noteService.addNote(manuscriptId, request, authorId);

        assertThat(result.authorName()).isEqualTo("Rhea Researcher");
        assertThat(result.manuscriptTitle()).isEqualTo("Codex Aureus");
        verify(studyNoteRepository).save(any(StudyNote.class));
    }

    @Test
    void addNote_shouldThrow_whenManuscriptNotFound() {
        UUID manuscriptId = UUID.randomUUID();
        NoteAddRequest request = new NoteAddRequest("A note about the binding technique used here.");

        when(manuscriptRepository.findById(manuscriptId)).thenReturn(Optional.empty());

        assertThrows(ManuscriptDoesNotExistException.class, () ->
                noteService.addNote(manuscriptId, request, UUID.randomUUID()));
    }
}
