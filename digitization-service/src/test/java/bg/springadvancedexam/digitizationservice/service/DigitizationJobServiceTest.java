package bg.springadvancedexam.digitizationservice.service;

import bg.springadvancedexam.digitizationservice.dto.DigitizationJobResponse;
import bg.springadvancedexam.digitizationservice.exception.DigitizationJobNotFoundException;
import bg.springadvancedexam.digitizationservice.mapper.DigitizationMapper;
import bg.springadvancedexam.digitizationservice.model.entity.DigitizationJob;
import bg.springadvancedexam.digitizationservice.model.enums.JobStatus;
import bg.springadvancedexam.digitizationservice.model.enums.Priority;
import bg.springadvancedexam.digitizationservice.repository.DigitizationJobRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DigitizationJobServiceTest {
    @Mock
    private DigitizationJobRepository digitizationJobRepository;

    @InjectMocks
    private DigitizationJobService digitizationJobService;

    @Test
    void createJob_ShouldSaveJob() {
        UUID manuscriptId = UUID.randomUUID();
        Priority priority = Priority.HIGH;

        DigitizationJob job = new DigitizationJob();

        try (MockedStatic<DigitizationMapper> mockedMapper =
                     mockStatic(DigitizationMapper.class)) {

            mockedMapper.when(() ->
                            DigitizationMapper.toDigitizationJob(manuscriptId, priority))
                    .thenReturn(job);

            digitizationJobService.createJob(manuscriptId, priority);

            verify(digitizationJobRepository).save(job);

            mockedMapper.verify(() ->
                    DigitizationMapper.toDigitizationJob(manuscriptId, priority));
        }
    }

    @Test
    void fetchStatus_ShouldReturnResponse_WhenJobExists() {
        UUID manuscriptId = UUID.randomUUID();

        DigitizationJob job = new DigitizationJob();
        DigitizationJobResponse expectedResponse =
                new DigitizationJobResponse(
                        manuscriptId,
                        JobStatus.QUEUED,
                        Priority.HIGH,
                        "technician-name",
                        LocalDateTime.now(),
                        null
                );

        when(digitizationJobRepository.findByManuscriptId(manuscriptId))
                .thenReturn(Optional.of(job));

        try (MockedStatic<DigitizationMapper> mockedMapper =
                     mockStatic(DigitizationMapper.class)) {

            mockedMapper.when(() ->
                            DigitizationMapper.toDigitizationJobResponse(job))
                    .thenReturn(expectedResponse);

            DigitizationJobResponse actualResponse =
                    digitizationJobService.fetchStatus(manuscriptId);

            assertEquals(expectedResponse, actualResponse);

            verify(digitizationJobRepository)
                    .findByManuscriptId(manuscriptId);

            mockedMapper.verify(() ->
                    DigitizationMapper.toDigitizationJobResponse(job));
        }
    }

    @Test
    void fetchStatus_ShouldThrow_WhenJobDoesNotExist() {
        UUID manuscriptId = UUID.randomUUID();

        when(digitizationJobRepository.findByManuscriptId(manuscriptId))
                .thenReturn(Optional.empty());

        DigitizationJobNotFoundException exception =
                assertThrows(
                        DigitizationJobNotFoundException.class,
                        () -> digitizationJobService.fetchStatus(manuscriptId)
                );

        assertEquals(
                "No job found for this manuscript.",
                exception.getMessage()
        );

        verify(digitizationJobRepository)
                .findByManuscriptId(manuscriptId);

        verifyNoMoreInteractions(digitizationJobRepository);
    }
}
