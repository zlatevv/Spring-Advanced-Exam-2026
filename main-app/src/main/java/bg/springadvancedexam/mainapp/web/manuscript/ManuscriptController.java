package bg.springadvancedexam.mainapp.web.manuscript;

import bg.springadvancedexam.mainapp.dto.digitzation.CreateJobRequest;
import bg.springadvancedexam.mainapp.dto.digitzation.JobStatusResponse;
import bg.springadvancedexam.mainapp.dto.manuscript.*;
import bg.springadvancedexam.mainapp.model.enums.Era;
import bg.springadvancedexam.mainapp.security.CustomUserDetails;
import bg.springadvancedexam.mainapp.service.manuscript.ManuscriptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/manuscripts")
@RequiredArgsConstructor
public class ManuscriptController {

    private final ManuscriptService manuscriptService;

    @GetMapping
    public ResponseEntity<Page<ManuscriptResponse>> getManuscripts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Era era,
            @PageableDefault(size = 12) Pageable pageable) {
        return ResponseEntity.ok(manuscriptService.fetchManuscripts(search, era, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManuscriptResponse> getManuscript(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails principal) {
        UUID requesterId = (principal != null) ? principal.getUserId() : null;
        return ResponseEntity.ok(manuscriptService.fetchManuscript(id, requesterId));
    }

    @PostMapping
    public ResponseEntity<ManuscriptResponse> createManuscript(
            @Valid @RequestBody CreateManuscriptRequest request) {
        ManuscriptResponse created = manuscriptService.createManuscript(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ManuscriptResponse> updateManuscript(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateManuscriptRequest request) {
        return ResponseEntity.ok(manuscriptService.updateManuscript(id, request));
    }

    @PutMapping("/{id}/visibility")
    public ResponseEntity<ManuscriptResponse> setVisibility(
            @PathVariable UUID id,
            @RequestBody VisibilityRequest request) {
        return ResponseEntity.ok(manuscriptService.setManuscriptVisibility(id, request.visibility()));
    }

    @PostMapping("/{id}/digitize")
    public ResponseEntity<ManuscriptResponse> requestDigitization(
            @PathVariable UUID id,
            @Valid @RequestBody CreateJobRequest request) {
        return ResponseEntity.ok(manuscriptService.requestDigitization(id, request.priority()));
    }

    @GetMapping("/{id}/digitization-status")
    public ResponseEntity<JobStatusResponse> getDigitizationStatus(@PathVariable UUID id) {
        return ResponseEntity.ok(manuscriptService.fetchDigitizationStatus(id));
    }

    @GetMapping("/{id}/summary")
    public ResponseEntity<SummaryResponse> getManuscriptSummary(@PathVariable UUID id) {
        return ResponseEntity.ok(manuscriptService.generateSummary(id));
    }
}