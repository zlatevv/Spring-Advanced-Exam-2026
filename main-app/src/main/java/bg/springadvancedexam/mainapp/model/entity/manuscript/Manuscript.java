package bg.springadvancedexam.mainapp.model.entity.manuscript;

import bg.springadvancedexam.mainapp.model.enums.ConservationStatus;
import bg.springadvancedexam.mainapp.model.enums.DigitizationStatus;
import bg.springadvancedexam.mainapp.model.enums.Era;
import bg.springadvancedexam.mainapp.model.enums.Visibility;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "manuscripts")
@Data
@Builder
public class Manuscript {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    private String author;

    @Enumerated(EnumType.STRING)
    private Era era;

    private String originRegion;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private ConservationStatus conservationStatus;

    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @Enumerated(EnumType.STRING)
    private DigitizationStatus digitizationStatus;

    private LocalDateTime createdAt;
}
