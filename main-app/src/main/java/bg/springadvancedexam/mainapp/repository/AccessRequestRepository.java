package bg.springadvancedexam.mainapp.repository;

import bg.springadvancedexam.mainapp.model.entity.AccessRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccessRequestRepository extends JpaRepository<AccessRequest, UUID> {
}
