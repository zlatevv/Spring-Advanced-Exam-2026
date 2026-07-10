package bg.springadvancedexam.backend.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.passay.*;

import java.util.Arrays;
import java.util.List;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }
        PasswordValidator validator = new PasswordValidator(Arrays.asList(
                // Length rule (Min 10, Max 72)
                new LengthRule(10, 72),

                // No spaces allowed
                new WhitespaceRule(),

                // Reject sequences (e.g., "12345", "abcde" and block keyboard patterns like qwer asdf)
                new IllegalSequenceRule(EnglishSequenceData.Numerical, 3, false),
                new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 3, false),
                new IllegalSequenceRule(EnglishSequenceData.USQwerty, 4, false),

                // Reject repeating characters (e.g., "aaaa")
                new RepeatCharacterRegexRule(3),

                // Reject username and password being the same
                new UsernameRule()
        ));

        // Validate the password
        RuleResult result = validator.validate(new PasswordData(password));

        if (result.isValid()) {
            return true;
        }

        // If invalid, read Passay's error messages and pass them to Jakarta
        List<String> messages = validator.getMessages(result);
        String messageTemplate = String.join(", ", messages);

        // Overwrite the default annotation message with the exact rules broken
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(messageTemplate)
                .addConstraintViolation();

        return false;
    }
}
