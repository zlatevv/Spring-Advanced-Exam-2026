package bg.springadvancedexam.backend.repository;

import bg.springadvancedexam.backend.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
