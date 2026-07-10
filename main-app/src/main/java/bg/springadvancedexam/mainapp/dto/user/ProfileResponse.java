package bg.springadvancedexam.mainapp.dto.user;

import bg.springadvancedexam.mainapp.model.enums.Role;

import java.time.LocalDateTime;

public record ProfileResponse(
        String fullName,
        String email,
        LocalDateTime createdAt,
        Role role,
        String institution
) {
}
