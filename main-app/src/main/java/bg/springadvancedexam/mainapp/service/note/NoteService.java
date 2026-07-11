package bg.springadvancedexam.mainapp.service.note;

import bg.springadvancedexam.mainapp.dto.note.NoteAddRequest;
import bg.springadvancedexam.mainapp.dto.note.NoteResponse;
import bg.springadvancedexam.mainapp.exception.manuscript.ManuscriptDoesNotExistException;
import bg.springadvancedexam.mainapp.exception.note.NoteAccessDeniedException;
import bg.springadvancedexam.mainapp.exception.note.NoteDoesNotExistException;
import bg.springadvancedexam.mainapp.exception.user.UserNotFoundException;
import bg.springadvancedexam.mainapp.mapper.note.NoteMapper;
import bg.springadvancedexam.mainapp.model.entity.manuscript.Manuscript;
import bg.springadvancedexam.mainapp.model.entity.note.StudyNote;
import bg.springadvancedexam.mainapp.model.entity.user.User;
import bg.springadvancedexam.mainapp.model.enums.Role;
import bg.springadvancedexam.mainapp.repository.manuscript.ManuscriptRepository;
import bg.springadvancedexam.mainapp.repository.note.StudyNoteRepository;
import bg.springadvancedexam.mainapp.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NoteService {
    private final StudyNoteRepository studyNoteRepository;
    private final ManuscriptRepository manuscriptRepository;
    private final UserRepository userRepository;
    private final CacheManager cacheManager;

    @Cacheable(value = "manuscriptNotes", key = "#manuscriptId")
    public List<NoteResponse> fetchNotes(UUID manuscriptId) {
        if (!manuscriptRepository.existsById(manuscriptId)) {
            throw new ManuscriptDoesNotExistException("Manuscript not found!");
        }
        return studyNoteRepository
                .findAllByManuscriptId(manuscriptId)
                .stream()
                .map(NoteMapper::toNoteResponse)
                .toList();
    }

    @CacheEvict(value = "manuscriptNotes", key = "#manuscriptId")
    @Transactional
    public NoteResponse addNote(UUID manuscriptId, NoteAddRequest noteAddRequest, UUID authorId) {
        Manuscript manuscript = manuscriptRepository.findById(manuscriptId)
                .orElseThrow(() -> new ManuscriptDoesNotExistException("Manuscript not found!"));

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));

        StudyNote note = NoteMapper.toStudyNote(noteAddRequest, manuscript, author);
        studyNoteRepository.save(note);

        return NoteMapper.toNoteResponse(note);
    }

    @Transactional
    public void deleteNote(UUID noteId, UUID requesterId, Role requesterRole) {
        StudyNote note = studyNoteRepository.findById(noteId)
                .orElseThrow(() -> new NoteDoesNotExistException("Note doesn't exist!"));

        boolean isOwner = note.getAuthor().getId().equals(requesterId);
        boolean isAdmin = requesterRole == Role.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new NoteAccessDeniedException("You can only delete your own notes.");
        }

        UUID manuscriptId = note.getManuscript().getId();
        studyNoteRepository.deleteById(noteId);

        var cache = cacheManager.getCache("manuscriptNotes");
        if (cache != null) {
            cache.evict(manuscriptId);
        }
    }
}
