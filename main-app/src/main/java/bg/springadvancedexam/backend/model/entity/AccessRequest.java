package bg.springadvancedexam.backend.model.entity;

import bg.springadvancedexam.backend.model.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "access_requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "manuscript_id")
    private Manuscript manuscript;

    @ManyToOne
    @JoinColumn(name = "researcher_id")
    private User researcher;

    private String purpose;

    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;

    private LocalDateTime requestedDate;
    private LocalDateTime decidedAt;

    @ManyToOne
    @JoinColumn(name = "decided_by_id")
    private User decidedBy;
}
