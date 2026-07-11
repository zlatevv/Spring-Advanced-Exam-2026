package bg.springadvancedexam.mainapp.dto.note;

import java.time.LocalDateTime;

public record NoteResponse(
        String manuscriptTitle,
        String authorName,
        String content,
        LocalDateTime createdAt
) {
}
