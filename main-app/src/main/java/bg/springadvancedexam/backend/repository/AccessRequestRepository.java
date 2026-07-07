package bg.springadvancedexam.backend.repository;

import bg.springadvancedexam.backend.model.entity.AccessRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccessRequestRepository extends JpaRepository<AccessRequest, UUID> {
}
