package bg.springadvancedexam.mainapp.manuscript;

import bg.springadvancedexam.mainapp.client.DigitizationClient;
import bg.springadvancedexam.mainapp.dto.manuscript.ManuscriptResponse;
import bg.springadvancedexam.mainapp.exception.manuscript.ManuscriptAccessDeniedException;
import bg.springadvancedexam.mainapp.exception.manuscript.ManuscriptDoesNotExistException;
import bg.springadvancedexam.mainapp.exception.manuscript.ManuscriptNotEligibleException;
import bg.springadvancedexam.mainapp.model.entity.manuscript.Manuscript;
import bg.springadvancedexam.mainapp.model.enums.DigitizationStatus;
import bg.springadvancedexam.mainapp.model.enums.Priority;
import bg.springadvancedexam.mainapp.model.enums.Visibility;
import bg.springadvancedexam.mainapp.repository.manuscript.ManuscriptRepository;
import bg.springadvancedexam.mainapp.service.manuscript.ManuscriptService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ManuscriptServiceTest {
    @Mock
    private ManuscriptRepository manuscriptRepository;

    @InjectMocks
    private ManuscriptService manuscriptService;

    @Mock
    private DigitizationClient digitizationClient;

    @Test
    void requestDigitization_shouldThrow_whenManuscriptNotFound() {
        UUID manuscriptId = UUID.randomUUID();

        when(manuscriptRepository.findById(manuscriptId)).thenReturn(Optional.empty());

        assertThrows(ManuscriptDoesNotExistException.class, () ->
                manuscriptService.requestDigitization(manuscriptId, Priority.MEDIUM));
    }

    @Test
    void requestDigitization_shouldThrow_whenManuscriptNotEligible() {
        UUID manuscriptId = UUID.randomUUID();
        Manuscript manuscript = Manuscript.builder()
                .id(manuscriptId)
                .title("Codex Aureus")
                .digitizationStatus(DigitizationStatus.COMPLETE)
                .build();

        when(manuscriptRepository.findById(manuscriptId)).thenReturn(Optional.of(manuscript));

        assertThrows(ManuscriptNotEligibleException.class, () ->
                manuscriptService.requestDigitization(manuscriptId, Priority.MEDIUM));
    }

    @Test
    void requestDigitization_shouldRequest_whenValid() {
        UUID manuscriptId = UUID.randomUUID();
        Manuscript manuscript = Manuscript.builder()
                .id(manuscriptId)
                .title("Codex Aureus")
                .digitizationStatus(DigitizationStatus.NOT_STARTED)
                .build();

        when(manuscriptRepository.findById(manuscriptId)).thenReturn(Optional.of(manuscript));
        when(manuscriptRepository.save(any(Manuscript.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ManuscriptResponse result = manuscriptService.requestDigitization(manuscriptId, Priority.MEDIUM);

        assertThat(result.digitizationStatus()).isEqualTo(DigitizationStatus.QUEUED);
        verify(manuscriptRepository).save(any(Manuscript.class));
    }

    @Test
    void fetchManuscript_shouldThrow_whenRestrictedAndNoRequester() {
        UUID manuscriptId = UUID.randomUUID();
        Manuscript manuscript = Manuscript.builder()
                .id(manuscriptId)
                .visibility(Visibility.RESTRICTED)
                .build();

        when(manuscriptRepository.findById(manuscriptId)).thenReturn(Optional.of(manuscript));

        assertThrows(ManuscriptAccessDeniedException.class, () ->
                manuscriptService.fetchManuscript(manuscriptId, null));
    }
}
