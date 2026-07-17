package bg.springadvancedexam.mainapp.user;

import bg.springadvancedexam.mainapp.model.entity.user.User;
import bg.springadvancedexam.mainapp.model.enums.Role;
import bg.springadvancedexam.mainapp.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application.properties")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @MockitoBean
    private CacheManager cacheManager;

    @Test
    void existsByEmail_shouldReturnTrue_whenUserExists() {
        User user = User.builder()
                .fullName("Rhea Researcher")
                .email("rhea@example.com")
                .password("hashed-password-value")
                .role(Role.RESEARCHER)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        boolean exists = userRepository.existsByEmail("rhea@example.com");

        assertThat(exists).isTrue();
    }

    @Test
    void existsByEmail_shouldReturnFalse_whenUserDoesNotExist() {
        boolean exists = userRepository.existsByEmail("nobody@example.com");

        assertThat(exists).isFalse();
    }

    @Test
    void countByRole_shouldReturnCorrectCount() {
        userRepository.save(User.builder()
                .fullName("Admin One").email("admin1@example.com").password("x")
                .role(Role.ADMIN).createdAt(LocalDateTime.now()).build());
        userRepository.save(User.builder()
                .fullName("Admin Two").email("admin2@example.com").password("x")
                .role(Role.ADMIN).createdAt(LocalDateTime.now()).build());
        userRepository.save(User.builder()
                .fullName("Some Researcher").email("researcher@example.com").password("x")
                .role(Role.RESEARCHER).createdAt(LocalDateTime.now()).build());

        long adminCount = userRepository.countByRole(Role.ADMIN);

        assertThat(adminCount).isEqualTo(2);
    }
}
