package bg.springadvancedexam.mainapp.web.note;

import bg.springadvancedexam.mainapp.dto.note.NoteAddRequest;
import bg.springadvancedexam.mainapp.dto.note.NoteResponse;
import bg.springadvancedexam.mainapp.security.CustomUserDetails;
import bg.springadvancedexam.mainapp.service.note.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @GetMapping("/api/manuscripts/{manuscriptId}/notes")
    public ResponseEntity<List<NoteResponse>> getNotes(@PathVariable UUID manuscriptId) {
        return ResponseEntity.ok(noteService.fetchNotes(manuscriptId));
    }

    @PostMapping("/api/manuscripts/{manuscriptId}/notes")
    public ResponseEntity<NoteResponse> addNote(
            @PathVariable UUID manuscriptId,
            @Valid @RequestBody NoteAddRequest request,
            @AuthenticationPrincipal CustomUserDetails principal) {
        NoteResponse created = noteService.addNote(manuscriptId, request, principal.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/api/notes/{noteId}")
    public ResponseEntity<Void> deleteNote(
            @PathVariable UUID noteId,
            @AuthenticationPrincipal CustomUserDetails principal) {
        noteService.deleteNote(noteId, principal.getUserId(), principal.getRole());
        return ResponseEntity.noContent().build();
    }
}
