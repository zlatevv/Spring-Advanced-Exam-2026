package bg.springadvancedexam.backend.dto.auth;

public record LoginResponse (
        String token,
        UserResponse user
){
}
