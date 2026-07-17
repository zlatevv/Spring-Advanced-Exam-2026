package bg.springadvancedexam.mainapp.request;

import bg.springadvancedexam.mainapp.dto.request.AccessRequestCreateRequest;
import bg.springadvancedexam.mainapp.dto.request.AccessRequestResponse;
import bg.springadvancedexam.mainapp.event.AccessRequestApprovedEvent;
import bg.springadvancedexam.mainapp.exception.manuscript.ManuscriptDoesNotExistException;
import bg.springadvancedexam.mainapp.exception.request.InvalidRequestDecisionException;
import bg.springadvancedexam.mainapp.exception.request.RequestAlreadyDecidedException;
import bg.springadvancedexam.mainapp.exception.request.RequestNotFoundException;
import bg.springadvancedexam.mainapp.model.entity.manuscript.Manuscript;
import bg.springadvancedexam.mainapp.model.entity.request.AccessRequest;
import bg.springadvancedexam.mainapp.model.entity.user.User;
import bg.springadvancedexam.mainapp.model.enums.RequestStatus;
import bg.springadvancedexam.mainapp.model.enums.Role;
import bg.springadvancedexam.mainapp.repository.manuscript.ManuscriptRepository;
import bg.springadvancedexam.mainapp.repository.request.AccessRequestRepository;
import bg.springadvancedexam.mainapp.repository.user.UserRepository;
import bg.springadvancedexam.mainapp.service.request.AccessRequestService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccessRequestServiceTest {

    @Mock
    private AccessRequestRepository accessRequestRepository;
    @Mock
    private ManuscriptRepository manuscriptRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private AccessRequestService accessRequestService;

    @Test
    void submitAccessRequest_shouldCreateRequest_whenValid() {
        UUID userId = UUID.randomUUID();
        UUID manuscriptId = UUID.randomUUID();
        User researcher = User.builder().id(userId).fullName("Rhea Researcher").role(Role.RESEARCHER).build();
        Manuscript manuscript = Manuscript.builder().id(manuscriptId).title("Codex Aureus").build();
        AccessRequestCreateRequest request = new AccessRequestCreateRequest(manuscriptId, "Studying medieval binding techniques.");

        when(userRepository.findById(userId)).thenReturn(Optional.of(researcher));
        when(manuscriptRepository.findById(manuscriptId)).thenReturn(Optional.of(manuscript));
        when(accessRequestRepository.save(any(AccessRequest.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AccessRequestResponse result = accessRequestService.submitAccessRequest(request, userId);

        assertThat(result.status()).isEqualTo(RequestStatus.PENDING);
        assertThat(result.manuscriptTitle()).isEqualTo("Codex Aureus");
        verify(accessRequestRepository).save(any(AccessRequest.class));
    }

    @Test
    void submitAccessRequest_shouldThrow_whenManuscriptNotFound() {
        UUID userId = UUID.randomUUID();
        UUID manuscriptId = UUID.randomUUID();
        AccessRequestCreateRequest request = new AccessRequestCreateRequest(manuscriptId, "Some purpose text here.");

        when(userRepository.findById(userId)).thenReturn(Optional.of(User.builder().id(userId).build()));
        when(manuscriptRepository.findById(manuscriptId)).thenReturn(Optional.empty());

        assertThrows(ManuscriptDoesNotExistException.class, () ->
                accessRequestService.submitAccessRequest(request, userId));
    }

    @Test
    void decideAccessRequest_shouldApproveAndPublishEvent_whenValid() {
        UUID requestId = UUID.randomUUID();
        UUID deciderId = UUID.randomUUID();
        UUID researcherId = UUID.randomUUID();
        UUID manuscriptId = UUID.randomUUID();

        User researcher = User.builder().id(researcherId).build();
        User decider = User.builder().id(deciderId).role(Role.CURATOR).build();
        Manuscript manuscript = Manuscript.builder().id(manuscriptId).title("Codex Aureus").build();
        AccessRequest existing = AccessRequest.builder()
                .id(requestId)
                .researcher(researcher)
                .manuscript(manuscript)
                .requestStatus(RequestStatus.PENDING)
                .build();

        when(accessRequestRepository.findById(requestId)).thenReturn(Optional.of(existing));
        when(userRepository.findById(deciderId)).thenReturn(Optional.of(decider));
        when(accessRequestRepository.save(any(AccessRequest.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AccessRequestResponse result = accessRequestService.decideAccessRequest(requestId, RequestStatus.APPROVED, deciderId);

        assertThat(result.status()).isEqualTo(RequestStatus.APPROVED);
        verify(eventPublisher).publishEvent(any(AccessRequestApprovedEvent.class));
    }

    @Test
    void decideAccessRequest_shouldRejectWithoutPublishingEvent_whenValid() {
        UUID requestId = UUID.randomUUID();
        UUID deciderId = UUID.randomUUID();

        AccessRequest existing = AccessRequest.builder()
                .id(requestId)
                .researcher(User.builder().id(UUID.randomUUID()).build())
                .manuscript(Manuscript.builder().id(UUID.randomUUID()).build())
                .requestStatus(RequestStatus.PENDING)
                .build();

        when(accessRequestRepository.findById(requestId)).thenReturn(Optional.of(existing));
        when(userRepository.findById(deciderId)).thenReturn(Optional.of(User.builder().id(deciderId).build()));
        when(accessRequestRepository.save(any(AccessRequest.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AccessRequestResponse result = accessRequestService.decideAccessRequest(requestId, RequestStatus.REJECTED, deciderId);

        assertThat(result.status()).isEqualTo(RequestStatus.REJECTED);
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void decideAccessRequest_shouldThrow_whenRequestNotFound() {
        UUID requestId = UUID.randomUUID();
        when(accessRequestRepository.findById(requestId)).thenReturn(Optional.empty());

        assertThrows(RequestNotFoundException.class, () ->
                accessRequestService.decideAccessRequest(requestId, RequestStatus.APPROVED, UUID.randomUUID()));
    }

    @Test
    void decideAccessRequest_shouldThrow_whenAlreadyDecided() {
        UUID requestId = UUID.randomUUID();
        UUID deciderId = UUID.randomUUID();
        AccessRequest alreadyDecided = AccessRequest.builder()
                .id(requestId)
                .requestStatus(RequestStatus.APPROVED)
                .build();

        when(accessRequestRepository.findById(requestId)).thenReturn(Optional.of(alreadyDecided));
        when(userRepository.findById(deciderId)).thenReturn(Optional.of(User.builder().id(deciderId).build()));

        assertThrows(RequestAlreadyDecidedException.class, () ->
                accessRequestService.decideAccessRequest(requestId, RequestStatus.REJECTED, deciderId));
    }

    @Test
    void decideAccessRequest_shouldThrow_whenDecisionIsInvalid() {
        UUID requestId = UUID.randomUUID();
        UUID deciderId = UUID.randomUUID();
        AccessRequest pending = AccessRequest.builder()
                .id(requestId)
                .requestStatus(RequestStatus.PENDING)
                .build();

        when(accessRequestRepository.findById(requestId)).thenReturn(Optional.of(pending));
        when(userRepository.findById(deciderId)).thenReturn(Optional.of(User.builder().id(deciderId).build()));

        assertThrows(InvalidRequestDecisionException.class, () ->
                accessRequestService.decideAccessRequest(requestId, RequestStatus.PENDING, deciderId));
    }

    @Test
    void fetchMyAccessRequests_shouldReturnMappedList() {
        UUID userId = UUID.randomUUID();
        AccessRequest request = AccessRequest.builder()
                .id(UUID.randomUUID())
                .researcher(User.builder().id(userId).fullName("Rhea").build())
                .manuscript(Manuscript.builder().id(UUID.randomUUID()).title("Codex").build())
                .requestStatus(RequestStatus.PENDING)
                .build();

        when(userRepository.existsById(userId)).thenReturn(true);
        when(accessRequestRepository.findAllByResearcherId(userId)).thenReturn(List.of(request));

        List<AccessRequestResponse> result = accessRequestService.fetchMyAccessRequests(userId);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().status()).isEqualTo(RequestStatus.PENDING);
    }

    @Test
    void fetchAccessRequests_shouldFilterByStatus_whenStatusProvided() {
        AccessRequest pending = AccessRequest.builder()
                .id(UUID.randomUUID())
                .researcher(User.builder().id(UUID.randomUUID()).build())
                .manuscript(Manuscript.builder().id(UUID.randomUUID()).build())
                .requestStatus(RequestStatus.PENDING)
                .build();

        when(accessRequestRepository.findByRequestStatus(RequestStatus.PENDING)).thenReturn(List.of(pending));

        List<AccessRequestResponse> result = accessRequestService.fetchAccessRequests(RequestStatus.PENDING);

        assertThat(result).hasSize(1);
        verify(accessRequestRepository, never()).findAll();
    }
}
