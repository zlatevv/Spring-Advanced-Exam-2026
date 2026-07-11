package bg.springadvancedexam.mainapp.dto.note;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NoteAddRequest(
        @NotNull(message = "Note content is required")
        @Size(min = 10, max = 250, message = "Note length is minimum 10 and maximum 250")
        String content
) {
}
