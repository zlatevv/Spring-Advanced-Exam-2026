package bg.springadvancedexam.digitizationservice.service;

import bg.springadvancedexam.digitizationservice.dto.DigitizationJobResponse;
import bg.springadvancedexam.digitizationservice.exception.DigitizationJobNotFoundException;
import bg.springadvancedexam.digitizationservice.mapper.DigitizationMapper;
import bg.springadvancedexam.digitizationservice.model.entity.DigitizationJob;
import bg.springadvancedexam.digitizationservice.model.enums.Priority;
import bg.springadvancedexam.digitizationservice.repository.DigitizationJobRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DigitizationJobService {
    private final DigitizationJobRepository digitizationJobRepository;

    @Transactional
    public DigitizationJobResponse createJob(UUID manuscriptId, Priority priority) {
        DigitizationJob job = DigitizationMapper.toDigitizationJob(manuscriptId, priority);
        DigitizationJob saved = digitizationJobRepository.save(job);
        return DigitizationMapper.toDigitizationJobResponse(saved);
    }

    public DigitizationJobResponse fetchStatus(UUID manuscriptId) {
        DigitizationJob job = digitizationJobRepository.findByManuscriptId(manuscriptId)
                .orElseThrow(() -> new DigitizationJobNotFoundException("No job found for this manuscript."));
        return DigitizationMapper.toDigitizationJobResponse(job);
    }
}
