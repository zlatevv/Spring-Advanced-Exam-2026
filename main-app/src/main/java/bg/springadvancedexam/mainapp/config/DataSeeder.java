package bg.springadvancedexam.mainapp.config;

import bg.springadvancedexam.mainapp.model.entity.manuscript.Manuscript;
import bg.springadvancedexam.mainapp.model.entity.user.User;
import bg.springadvancedexam.mainapp.model.enums.*;
import bg.springadvancedexam.mainapp.repository.manuscript.ManuscriptRepository;
import bg.springadvancedexam.mainapp.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${seed.admin.name}")
    private String adminName;

    @Value("${seed.admin.email}")
    private String adminEmail;

    @Value("${seed.admin.password}")
    private String adminPassword;

    @Override
    public void run(String @NonNull ... args) {
        if (userRepository.count() > 0) {
            log.info("Users already exist, skipping data seeding.");
            return;
        }

        User admin = User.builder()
                .fullName(adminName)
                .email(adminEmail)
                .password(passwordEncoder.encode(adminPassword))
                .role(Role.ADMIN)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(admin);

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

        log.info("Seeded an admin and 3 manuscripts.");
    }
}
