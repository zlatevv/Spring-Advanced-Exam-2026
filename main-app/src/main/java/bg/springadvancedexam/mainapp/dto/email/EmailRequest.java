package bg.springadvancedexam.mainapp.dto.email;

public record EmailRequest(
        String email,
        String resetToken
) {
}
