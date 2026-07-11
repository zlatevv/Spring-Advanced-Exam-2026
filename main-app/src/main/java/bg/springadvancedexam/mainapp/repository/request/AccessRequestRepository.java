package bg.springadvancedexam.mainapp.repository.request;

import bg.springadvancedexam.mainapp.model.entity.request.AccessRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccessRequestRepository extends JpaRepository<AccessRequest, UUID> {
}
