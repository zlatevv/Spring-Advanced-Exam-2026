package bg.springadvancedexam.mainapp.dto.email;

import bg.springadvancedexam.mainapp.validation.ValidPassword;
import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest(
        @NotBlank String token,
        @NotBlank @ValidPassword String newPassword
) {}
