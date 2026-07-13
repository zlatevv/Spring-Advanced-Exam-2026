package bg.springadvancedexam.mainapp.repository.request;

import bg.springadvancedexam.mainapp.model.entity.request.AccessRequest;
import bg.springadvancedexam.mainapp.model.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AccessRequestRepository extends JpaRepository<AccessRequest, UUID> {

    List<AccessRequest> findByRequestStatus(RequestStatus status);

    List<AccessRequest> findAllByResearcherId(UUID userId);
}
