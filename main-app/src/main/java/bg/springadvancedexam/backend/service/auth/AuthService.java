package bg.springadvancedexam.backend.service.auth;

import bg.springadvancedexam.backend.dto.auth.LoginRequest;
import bg.springadvancedexam.backend.dto.auth.LoginResponse;
import bg.springadvancedexam.backend.dto.auth.RegisterRequest;
import bg.springadvancedexam.backend.dto.auth.UserResponse;
import bg.springadvancedexam.backend.exception.user.UserAlreadyExistsException;
import bg.springadvancedexam.backend.mapper.user.UserMapper;
import bg.springadvancedexam.backend.model.entity.User;
import bg.springadvancedexam.backend.repository.UserRepository;
import bg.springadvancedexam.backend.security.CustomUserDetails;
import bg.springadvancedexam.backend.security.JwtService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional
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
}
