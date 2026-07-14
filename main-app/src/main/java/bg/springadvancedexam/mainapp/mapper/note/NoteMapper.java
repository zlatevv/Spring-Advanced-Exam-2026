package bg.springadvancedexam.mainapp.mapper.note;

import bg.springadvancedexam.mainapp.dto.note.NoteAddRequest;
import bg.springadvancedexam.mainapp.dto.note.NoteResponse;
import bg.springadvancedexam.mainapp.model.entity.manuscript.Manuscript;
import bg.springadvancedexam.mainapp.model.entity.note.StudyNote;
import bg.springadvancedexam.mainapp.model.entity.user.User;

import java.time.LocalDateTime;

public class NoteMapper {
    public static NoteResponse toNoteResponse(StudyNote note) {
        String manuscriptTitle = note.getManuscript().getTitle();
        String author = note.getAuthor().getFullName();
        String content = note.getContent();
        LocalDateTime createdAt = note.getCreatedAt();

        return new NoteResponse(
                manuscriptTitle,
                author,
                content,
                createdAt
        );
    }

    public static StudyNote toStudyNote(NoteAddRequest request, Manuscript manuscript, User author) {
        return StudyNote.builder()
                .content(request.content())
                .createdAt(LocalDateTime.now())
                .manuscript(manuscript)
                .author(author)
                .build();
    }
}
