package bg.springadvancedexam.mainapp.client;

import bg.springadvancedexam.mainapp.dto.digitzation.CreateJobRequest;
import bg.springadvancedexam.mainapp.dto.digitzation.JobStatusResponse;
import bg.springadvancedexam.mainapp.model.enums.JobStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "digitization-service", url = "${digitization.service.url}")
public interface DigitizationClient {

    @PostMapping("/api/jobs")
    void createJob(@RequestBody CreateJobRequest request);

    @GetMapping("/api/jobs/manuscript/{manuscriptId}")
    JobStatusResponse getStatus(@PathVariable UUID manuscriptId);
}
