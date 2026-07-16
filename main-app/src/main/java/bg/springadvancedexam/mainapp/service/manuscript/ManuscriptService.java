package bg.springadvancedexam.mainapp.service.manuscript;

import bg.springadvancedexam.mainapp.client.DigitizationClient;
import bg.springadvancedexam.mainapp.dto.digitzation.CreateJobRequest;
import bg.springadvancedexam.mainapp.dto.digitzation.JobStatusResponse;
import bg.springadvancedexam.mainapp.dto.manuscript.CreateManuscriptRequest;
import bg.springadvancedexam.mainapp.dto.manuscript.ManuscriptResponse;
import bg.springadvancedexam.mainapp.dto.manuscript.UpdateManuscriptRequest;
import bg.springadvancedexam.mainapp.exception.manuscript.ManuscriptAccessDeniedException;
import bg.springadvancedexam.mainapp.exception.manuscript.ManuscriptDoesNotExistException;
import bg.springadvancedexam.mainapp.exception.manuscript.ManuscriptNotEligibleException;
import bg.springadvancedexam.mainapp.mapper.manuscript.ManuscriptMapper;
import bg.springadvancedexam.mainapp.model.entity.manuscript.Manuscript;
import bg.springadvancedexam.mainapp.model.enums.*;
import bg.springadvancedexam.mainapp.repository.manuscript.ManuscriptRepository;
import org.springframework.cache.annotation.Cacheable;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ManuscriptService {
    private final ManuscriptRepository manuscriptRepository;
    private final DigitizationClient digitizationClient;

    @Transactional
    public ManuscriptResponse createManuscript(CreateManuscriptRequest createManuscriptRequest) {
        Manuscript manuscript = ManuscriptMapper.toManuscriptEntity(createManuscriptRequest);

        Manuscript saved = manuscriptRepository.save(manuscript);

        return ManuscriptMapper.toManuscriptResponse(saved);
    }

    @Transactional
    @CacheEvict(value = "manuscripts", key = "#manuscriptId")
    public ManuscriptResponse updateManuscript(UUID manuscriptId, UpdateManuscriptRequest updateManuscriptRequest) {
        Manuscript manuscript = manuscriptRepository.findById(manuscriptId)
                .orElseThrow(() -> new ManuscriptDoesNotExistException("Manuscript does not exist"));

        manuscript.setAuthor(updateManuscriptRequest.author());
        manuscript.setDescription(updateManuscriptRequest.description());
        manuscript.setConservationStatus(updateManuscriptRequest.conservationStatus());

        Manuscript saved = manuscriptRepository.save(manuscript);
        return ManuscriptMapper.toManuscriptResponse(saved);
    }

    @Transactional
    public ManuscriptResponse setManuscriptVisibility(UUID manuscriptId, Visibility visibility) {
        Manuscript manuscript = manuscriptRepository.findById(manuscriptId)
                .orElseThrow(() -> new ManuscriptDoesNotExistException("Manuscript does not exist"));

        manuscript.setVisibility(visibility);
        Manuscript saved = manuscriptRepository.save(manuscript);

        return ManuscriptMapper.toManuscriptResponse(saved);
    }

    public Page<ManuscriptResponse> fetchManuscripts(String search, Era era, @PageableDefault(size = 12) Pageable pageable) {
        Page<Manuscript> manuscripts = manuscriptRepository.search(search, era, pageable);
        return manuscripts.map(ManuscriptMapper::toManuscriptResponse);
    }

    @Cacheable(value = "manuscripts", key = "#id")
    public ManuscriptResponse fetchManuscript(UUID id, UUID requesterId) {
        Manuscript manuscript = manuscriptRepository.findById(id)
                .orElseThrow(() -> new ManuscriptDoesNotExistException("Manuscript not found"));

        if (manuscript.getVisibility() == Visibility.RESTRICTED && requesterId == null) {
            throw new ManuscriptAccessDeniedException("Authentication required for this manuscript.");
        }

        return ManuscriptMapper.toManuscriptResponse(manuscript);
    }

    @Transactional
    @CacheEvict(value = "manuscripts", key = "#manuscriptId")
    public ManuscriptResponse requestDigitization(UUID manuscriptId, Priority priority) {
        Manuscript manuscript = manuscriptRepository.findById(manuscriptId)
                .orElseThrow(() -> new ManuscriptDoesNotExistException("Manuscript not found"));

        if (manuscript.getDigitizationStatus() != DigitizationStatus.NOT_STARTED) {
            throw new ManuscriptNotEligibleException("Digitization already requested or completed.");
        }

        digitizationClient.createJob(new CreateJobRequest(manuscriptId, priority));

        manuscript.setDigitizationStatus(DigitizationStatus.QUEUED);
        Manuscript saved = manuscriptRepository.save(manuscript);

        return ManuscriptMapper.toManuscriptResponse(saved);
    }

    public JobStatusResponse fetchDigitizationStatus(UUID manuscriptId) {
        if (!manuscriptRepository.existsById(manuscriptId)) {
            throw new ManuscriptDoesNotExistException("Manuscript not found");
        }
        return digitizationClient.getStatus(manuscriptId);
    }
}
