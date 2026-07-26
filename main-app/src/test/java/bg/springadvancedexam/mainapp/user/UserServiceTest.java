package bg.springadvancedexam.mainapp.user;

import bg.springadvancedexam.mainapp.dto.auth.UserResponse;
import bg.springadvancedexam.mainapp.exception.user.LastAdminException;
import bg.springadvancedexam.mainapp.exception.user.UserNotFoundException;
import bg.springadvancedexam.mainapp.model.entity.user.User;
import bg.springadvancedexam.mainapp.model.enums.Role;
import bg.springadvancedexam.mainapp.repository.user.UserRepository;
import bg.springadvancedexam.mainapp.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void updateUserRole_shouldUpdateRole_whenValid() {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .role(Role.RESEARCHER)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse result = userService.updateUserRole(userId, Role.CURATOR);

        assertThat(result.role()).isEqualTo(Role.CURATOR);
        verify(userRepository).save(user);
    }

    @Test
    void updateUserRole_shouldThrow_whenUserNotFound() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                userService.updateUserRole(userId, Role.CURATOR));
    }

    @Test
    void updateUserRole_shouldThrow_whenDemotingLastAdmin() {
        UUID userId = UUID.randomUUID();
        User admin = User.builder().id(userId).role(Role.ADMIN).build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(admin));
        when(userRepository.countByRole(Role.ADMIN)).thenReturn(1L);

        assertThrows(LastAdminException.class, () ->
                userService.updateUserRole(userId, Role.RESEARCHER));
    }

    @Test
    void updateUserRole_shouldUpdateRole_whenNotLastAdmin() {
        UUID userId = UUID.randomUUID();
        User admin = User.builder()
                .id(userId)
                .role(Role.ADMIN)
                .build();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(admin));

        when(userRepository.countByRole(Role.ADMIN))
                .thenReturn(2L);

        when(userRepository.save(admin))
                .thenReturn(admin);

        UserResponse result =
                userService.updateUserRole(userId, Role.CURATOR);

        assertThat(result.role()).isEqualTo(Role.CURATOR);

        verify(userRepository).save(admin);
    }
}
