package bg.springadvancedexam.digitizationservice.model.entity;

import bg.springadvancedexam.digitizationservice.model.enums.JobStatus;
import bg.springadvancedexam.digitizationservice.model.enums.Priority;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "digitization_jobs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DigitizationJob {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private UUID manuscriptId;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private JobStatus status;

    private String technician;

    private LocalDateTime requestedAt;

    private LocalDateTime completedAt;
}
