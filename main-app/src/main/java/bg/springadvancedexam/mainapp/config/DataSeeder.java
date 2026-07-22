package bg.springadvancedexam.mainapp.config;

import bg.springadvancedexam.mainapp.model.entity.manuscript.Manuscript;
import bg.springadvancedexam.mainapp.model.entity.user.User;
import bg.springadvancedexam.mainapp.model.enums.*;
import bg.springadvancedexam.mainapp.repository.manuscript.ManuscriptRepository;
import bg.springadvancedexam.mainapp.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ManuscriptRepository manuscriptRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void run(String @NonNull ... args) {
        if (userRepository.count() > 0) {
            log.info("Users already exist, skipping data seeding.");
            return;
        }

        User admin = User.builder()
                .fullName("System Admin")
                .email("admin@raremanuscripts.local")
                .password(passwordEncoder.encode("Admin123!"))
                .role(Role.ADMIN)
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(admin);

        User curator = User.builder()
                .fullName("Eleanor Curator")
                .email("curator@raremanuscripts.local")
                .password(passwordEncoder.encode("Curator123!"))
                .role(Role.CURATOR)
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(curator);

        User researcher = User.builder()
                .fullName("Rhea Researcher")
                .email("researcher@raremanuscripts.local")
                .password(passwordEncoder.encode("Research123!"))
                .role(Role.RESEARCHER)
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(researcher);

        manuscriptRepository.save(Manuscript.builder()
                .title("The Voynich Fragment")
                .author("Unknown")
                .era(Era.MEDIEVAL)
                .originRegion("Central Europe")
                .description("An undeciphered illustrated manuscript of unknown origin.")
                .conservationStatus(ConservationStatus.FRAGILE)
                .visibility(Visibility.PUBLIC)
                .digitizationStatus(DigitizationStatus.NOT_STARTED)
                .createdAt(LocalDateTime.now())
                .build());

        manuscriptRepository.save(Manuscript.builder()
                .title("Codex Aureus")
                .author("Brother Aldric")
                .era(Era.RENAISSANCE)
                .originRegion("Northern Italy")
                .description("A gilded liturgical codex commissioned for a Florentine cathedral.")
                .conservationStatus(ConservationStatus.STABLE)
                .visibility(Visibility.RESTRICTED)
                .digitizationStatus(DigitizationStatus.NOT_STARTED)
                .createdAt(LocalDateTime.now())
                .build());

        manuscriptRepository.save(Manuscript.builder()
                .title("Letters of the Third Expedition")
                .author("Marguerite de Vos")
                .era(Era.EARLY_MODERN)
                .originRegion("Low Countries")
                .description("Correspondence detailing a scientific expedition, partially water-damaged.")
                .conservationStatus(ConservationStatus.UNDER_TREATMENT)
                .visibility(Visibility.RESTRICTED)
                .digitizationStatus(DigitizationStatus.NOT_STARTED)
                .createdAt(LocalDateTime.now())
                .build());

        log.info("Seeded 3 users (admin/curator/researcher) and 3 manuscripts.");
    }
}
