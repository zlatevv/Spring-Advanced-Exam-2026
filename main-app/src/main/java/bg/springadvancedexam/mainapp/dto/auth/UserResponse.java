package bg.springadvancedexam.mainapp.dto.auth;

import bg.springadvancedexam.mainapp.model.enums.Role;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String fullName,
        String email,
        Role role
) {
}
