package bg.springadvancedexam.mainapp.web.request;

import bg.springadvancedexam.mainapp.dto.request.AccessRequestCreateRequest;
import bg.springadvancedexam.mainapp.dto.request.AccessRequestDecisionRequest;
import bg.springadvancedexam.mainapp.dto.request.AccessRequestResponse;
import bg.springadvancedexam.mainapp.model.enums.RequestStatus;
import bg.springadvancedexam.mainapp.security.CustomUserDetails;
import bg.springadvancedexam.mainapp.service.request.AccessRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/access-requests")
@RequiredArgsConstructor
public class AccessRequestController {

    private final AccessRequestService accessRequestService;

    @PostMapping
    public ResponseEntity<AccessRequestResponse> submitAccessRequest(
            @Valid @RequestBody AccessRequestCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails principal) {
        AccessRequestResponse created = accessRequestService.submitAccessRequest(request, principal.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/mine")
    public ResponseEntity<List<AccessRequestResponse>> getMyAccessRequests(
            @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(accessRequestService.fetchMyAccessRequests(principal.getUserId()));
    }

    @GetMapping
    public ResponseEntity<List<AccessRequestResponse>> getAccessRequests(
            @RequestParam(required = false) RequestStatus status) {
        return ResponseEntity.ok(accessRequestService.fetchAccessRequests(status));
    }

    @PutMapping("/{id}/decision")
    public ResponseEntity<AccessRequestResponse> decideAccessRequest(
            @PathVariable UUID id,
            @Valid @RequestBody AccessRequestDecisionRequest request,
            @AuthenticationPrincipal CustomUserDetails principal) {
        AccessRequestResponse decided = accessRequestService.decideAccessRequest(
                id, request.decision(), principal.getUserId());
        return ResponseEntity.ok(decided);
    }
}
