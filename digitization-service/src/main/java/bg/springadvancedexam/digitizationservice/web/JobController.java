package bg.springadvancedexam.digitizationservice.web;

import bg.springadvancedexam.digitizationservice.dto.CreateJobRequest;
import bg.springadvancedexam.digitizationservice.dto.DigitizationJobResponse;
import bg.springadvancedexam.digitizationservice.service.DigitizationJobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final DigitizationJobService digitizationJobService;

    @PostMapping
    public ResponseEntity<Void> createJob(@Valid @RequestBody CreateJobRequest request) {
        digitizationJobService.createJob(request.manuscriptId(), request.priority());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/manuscript/{manuscriptId}")
    public ResponseEntity<DigitizationJobResponse> getStatus(@PathVariable UUID manuscriptId) {
        return ResponseEntity.ok(digitizationJobService.fetchStatus(manuscriptId));
    }

    @DeleteMapping("/manuscript/{manuscriptId}")
    public ResponseEntity<Void> cancelJob(@PathVariable UUID manuscriptId) {
        digitizationJobService.cancelJob(manuscriptId);
        return ResponseEntity.noContent().build();
    }
}
