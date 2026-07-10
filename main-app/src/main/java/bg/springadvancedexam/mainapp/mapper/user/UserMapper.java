package bg.springadvancedexam.mainapp.mapper.user;

import bg.springadvancedexam.mainapp.dto.auth.RegisterRequest;
import bg.springadvancedexam.mainapp.dto.auth.UserResponse;
import bg.springadvancedexam.mainapp.model.entity.User;
import bg.springadvancedexam.mainapp.model.enums.Role;

import java.time.LocalDateTime;

public class UserMapper {
    public static User toUserEntity(RegisterRequest registerRequest, String normalizedEmail) {
        if  (registerRequest == null) {
            return null;
        }
        return User.builder()
                .fullName(registerRequest.fullName())
                .email(normalizedEmail)
                .createdAt(LocalDateTime.now())
                .role(Role.RESEARCHER)
                .build();
    }

    public static UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getRole());
    }
}
