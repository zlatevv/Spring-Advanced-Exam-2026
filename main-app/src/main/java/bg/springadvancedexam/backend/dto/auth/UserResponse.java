package bg.springadvancedexam.backend.dto.auth;

import bg.springadvancedexam.backend.model.enums.Role;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String fullName,
        String email,
        Role role
) {
}
