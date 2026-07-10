package bg.springadvancedexam.mainapp.dto.auth;

public record LoginResponse (
        String token,
        UserResponse user
){
}
