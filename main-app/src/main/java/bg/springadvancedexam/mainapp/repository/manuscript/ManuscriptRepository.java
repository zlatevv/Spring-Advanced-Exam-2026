package bg.springadvancedexam.mainapp.repository.manuscript;

import bg.springadvancedexam.mainapp.model.entity.manuscript.Manuscript;
import bg.springadvancedexam.mainapp.model.enums.DigitizationStatus;
import bg.springadvancedexam.mainapp.model.enums.Era;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ManuscriptRepository extends JpaRepository<Manuscript, UUID> {
    @Query("SELECT m FROM Manuscript m WHERE " +
            "(:search IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
            "(:era IS NULL OR m.era = :era)")
    Page<Manuscript> search(@Param("search") String search, @Param("era") Era era, Pageable pageable);

    List<Manuscript> findByDigitizationStatus(DigitizationStatus digitizationStatus);
}
