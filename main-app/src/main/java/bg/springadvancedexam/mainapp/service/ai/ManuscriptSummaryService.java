package bg.springadvancedexam.mainapp.service.ai;

import bg.springadvancedexam.mainapp.model.entity.manuscript.Manuscript;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManuscriptSummaryService {

    private final ChatClient.Builder chatClientBuilder;

    public String generateSummary(Manuscript manuscript) {
        ChatClient chatClient = chatClientBuilder.build();

        String prompt = """
                You are assisting a researcher browsing a rare manuscripts archive.
                Write a concise, 2-3 sentence summary explaining what this manuscript
                is and why a researcher might find it worth studying. Do not invent
                facts beyond what's given.

                Title: %s
                Author: %s
                Era: %s
                Origin: %s
                Description: %s
                """.formatted(
                manuscript.getTitle(),
                manuscript.getAuthor() != null ? manuscript.getAuthor() : "Unknown",
                manuscript.getEra(),
                manuscript.getOriginRegion(),
                manuscript.getDescription()
        );

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}