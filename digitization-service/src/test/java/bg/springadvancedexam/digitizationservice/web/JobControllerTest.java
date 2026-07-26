package bg.springadvancedexam.digitizationservice.web;

import bg.springadvancedexam.digitizationservice.model.enums.Priority;
import bg.springadvancedexam.digitizationservice.service.DigitizationJobService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(JobController.class)
class JobControllerTest {


    @Autowired
    private MockMvc mockMvc;


    @MockitoBean
    private DigitizationJobService digitizationJobService;


    @Test
    void createJob_ShouldReturn201() throws Exception {

        UUID manuscriptId = UUID.randomUUID();


        String requestBody = """
                {
                    "manuscriptId": "%s",
                    "priority": "HIGH"
                }
                """.formatted(manuscriptId);


        mockMvc.perform(post("/api/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());


        verify(digitizationJobService)
                .createJob(
                        manuscriptId,
                        Priority.HIGH
                );
    }
}