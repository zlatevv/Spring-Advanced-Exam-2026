package bg.springadvancedexam.mainapp.dto.note;

import java.time.LocalDateTime;
import java.util.UUID;

public record NoteResponse(
        UUID id,
        String manuscriptTitle,
        String authorName,
        String content,
        LocalDateTime createdAt
) {
}
