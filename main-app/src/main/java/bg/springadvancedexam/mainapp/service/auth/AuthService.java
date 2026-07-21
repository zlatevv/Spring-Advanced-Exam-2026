package bg.springadvancedexam.mainapp.service.auth;

import bg.springadvancedexam.mainapp.client.EmailClient;
import bg.springadvancedexam.mainapp.dto.auth.LoginRequest;
import bg.springadvancedexam.mainapp.dto.auth.LoginResponse;
import bg.springadvancedexam.mainapp.dto.auth.RegisterRequest;
import bg.springadvancedexam.mainapp.dto.auth.UserResponse;
import bg.springadvancedexam.mainapp.dto.email.EmailRequest;
import bg.springadvancedexam.mainapp.exception.user.UserAlreadyExistsException;
import bg.springadvancedexam.mainapp.exception.user.UserNotFoundException;
import bg.springadvancedexam.mainapp.mapper.user.UserMapper;
import bg.springadvancedexam.mainapp.model.entity.user.User;
import bg.springadvancedexam.mainapp.repository.user.UserRepository;
import bg.springadvancedexam.mainapp.security.CustomUserDetails;
import bg.springadvancedexam.mainapp.security.JwtService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmailClient emailClient;

    @Transactional
    @CacheEvict(value = "allUsers", key = "'all'")
    public UserResponse register(RegisterRequest registerRequest) {
        String normalizedEmail = registerRequest.email().trim().toLowerCase();

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new UserAlreadyExistsException("User already exists!");
        }

        User user = UserMapper.toUserEntity(registerRequest, normalizedEmail);
        user.setPassword(passwordEncoder.encode(registerRequest.password()));

        User savedUser;
        try {
            savedUser = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExistsException("User already exists!");
        }

        return UserMapper.toUserResponse(savedUser);
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
        );

        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();

        String token = jwtService.generateToken(principal.getUsername(), principal.getRole().name());

        UserResponse userResponse = new UserResponse(
                principal.getUserId(),
                principal.getFullName(),
                principal.getUsername(),
                principal.getRole()
        );

        return new LoginResponse(token, userResponse);
    }

    public UserResponse getCurrentUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User was not found!"));
        return UserMapper.toUserResponse(user);
    }

    public void requestPasswordReset(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            String resetToken = jwtService.generatePasswordResetToken(email);
            emailClient.sendResetEmail(new EmailRequest(email, resetToken));
        });
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        String email = jwtService.extractResetEmail(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
