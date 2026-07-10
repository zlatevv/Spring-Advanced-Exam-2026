package bg.springadvancedexam.backend.dto.auth;

import bg.springadvancedexam.backend.validation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Email is required")
        @Email(message = "Email is not valid")
        String email,

        @ValidPassword
        String password) {
}
