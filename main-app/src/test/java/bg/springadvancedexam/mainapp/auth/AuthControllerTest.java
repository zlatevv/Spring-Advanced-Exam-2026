package bg.springadvancedexam.mainapp.auth;

import bg.springadvancedexam.mainapp.context.CustomUserDetailsService;
import bg.springadvancedexam.mainapp.dto.auth.LoginRequest;
import bg.springadvancedexam.mainapp.dto.auth.LoginResponse;
import bg.springadvancedexam.mainapp.dto.auth.RegisterRequest;
import bg.springadvancedexam.mainapp.dto.auth.UserResponse;
import bg.springadvancedexam.mainapp.model.enums.Role;
import bg.springadvancedexam.mainapp.security.JwtService;
import bg.springadvancedexam.mainapp.service.auth.AuthService;
import bg.springadvancedexam.mainapp.web.auth.AuthController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureJson
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private CacheManager cacheManager;

    @Test
    void register_shouldReturnBadRequest_whenPasswordTooShort() throws Exception {
        RegisterRequest request = new RegisterRequest("Test User", "test@example.com", "short");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_shouldReturnCreated_whenValid() throws Exception {
        RegisterRequest request = new RegisterRequest("Test User", "test@example.com", "Xk9#mQ7pLzR2");
        UserResponse response = new UserResponse(UUID.randomUUID(), "Test User", "test@example.com", Role.RESEARCHER);

        when(authService.register(any(RegisterRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void login_shouldReturnUnauthorized_whenCredentialsInvalid() throws Exception {
        LoginRequest request = new LoginRequest("nobody@example.com", "wrongpassword");

        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new BadCredentialsException("Invalid email or password."));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_shouldReturnOk_whenCredentialsValid() throws Exception {
        LoginRequest request = new LoginRequest("test@example.com", "Xk9#mQ7pLzR2");
        UserResponse user = new UserResponse(UUID.randomUUID(), "Test User", "test@example.com", Role.RESEARCHER);
        LoginResponse response = new LoginResponse("fake-jwt-token", user);

        when(authService.login(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"))
                .andExpect(jsonPath("$.user.email").value("test@example.com"));
    }
}