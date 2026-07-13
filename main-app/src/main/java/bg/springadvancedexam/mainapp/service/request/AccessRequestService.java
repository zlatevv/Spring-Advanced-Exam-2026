package bg.springadvancedexam.mainapp.service.request;

import bg.springadvancedexam.mainapp.dto.request.AccessRequestCreateRequest;
import bg.springadvancedexam.mainapp.dto.request.AccessRequestResponse;
import bg.springadvancedexam.mainapp.exception.manuscript.ManuscriptDoesNotExistException;
import bg.springadvancedexam.mainapp.exception.request.InvalidRequestDecisionException;
import bg.springadvancedexam.mainapp.exception.request.RequestAlreadyDecidedException;
import bg.springadvancedexam.mainapp.exception.request.RequestNotFoundException;
import bg.springadvancedexam.mainapp.exception.user.UserNotFoundException;
import bg.springadvancedexam.mainapp.mapper.request.AccessRequestMapper;
import bg.springadvancedexam.mainapp.model.entity.manuscript.Manuscript;
import bg.springadvancedexam.mainapp.model.entity.request.AccessRequest;
import bg.springadvancedexam.mainapp.model.entity.user.User;
import bg.springadvancedexam.mainapp.model.enums.RequestStatus;
import bg.springadvancedexam.mainapp.repository.manuscript.ManuscriptRepository;
import bg.springadvancedexam.mainapp.repository.request.AccessRequestRepository;
import bg.springadvancedexam.mainapp.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccessRequestService {
    private final AccessRequestRepository accessRequestRepository;
    private final ManuscriptRepository manuscriptRepository;
    private final UserRepository userRepository;

    @Transactional
    public AccessRequestResponse submitAccessRequest(AccessRequestCreateRequest accessRequestCreateRequest,
                                                     UUID userId) {
        User researcher = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Manuscript manuscript = manuscriptRepository.findById(accessRequestCreateRequest.manuscriptId())
                .orElseThrow(() -> new ManuscriptDoesNotExistException("Manuscript not found"));

        AccessRequest accessRequest = AccessRequestMapper.toEntity(accessRequestCreateRequest, manuscript, researcher);

        AccessRequest saved = accessRequestRepository.save(accessRequest);

        return AccessRequestMapper.toResponse(saved);
    }

    public List<AccessRequestResponse> fetchMyAccessRequests(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found");
        }

        return accessRequestRepository.findAllByResearcherId(userId)
                .stream()
                .map(AccessRequestMapper::toResponse)
                .toList();
    }

    public List<AccessRequestResponse> fetchAccessRequests(RequestStatus status) {
        List<AccessRequest> requests = (status != null)
                ? accessRequestRepository.findByRequestStatus(status)
                : accessRequestRepository.findAll();

        return requests.stream().map(AccessRequestMapper::toResponse).toList();
    }

    @Transactional
    public AccessRequestResponse decideAccessRequest(UUID requestId, RequestStatus decision, UUID deciderId) {
        AccessRequest accessRequest = accessRequestRepository.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException("Request not found"));

        User decider = userRepository.findById(deciderId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (decision != RequestStatus.APPROVED && decision != RequestStatus.REJECTED) {
            throw new InvalidRequestDecisionException("Decision must be APPROVED or REJECTED.");
        }

        if (accessRequest.getRequestStatus() != RequestStatus.PENDING) {
            throw new RequestAlreadyDecidedException("Request already decided!");
        }

        accessRequest.setRequestStatus(decision);
        accessRequest.setDecidedAt(LocalDateTime.now());
        accessRequest.setDecidedBy(decider);

        AccessRequest saved = accessRequestRepository.save(accessRequest);

        return AccessRequestMapper.toResponse(saved);
    }
}
