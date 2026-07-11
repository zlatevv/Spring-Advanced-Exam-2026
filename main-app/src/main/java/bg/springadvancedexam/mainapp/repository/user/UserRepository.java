package bg.springadvancedexam.mainapp.repository.user;

import bg.springadvancedexam.mainapp.model.entity.user.User;
import bg.springadvancedexam.mainapp.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String normalizedEmail);
    long countByRole(Role role);
}
