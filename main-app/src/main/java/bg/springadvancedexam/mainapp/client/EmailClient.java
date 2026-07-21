package bg.springadvancedexam.mainapp.client;

import bg.springadvancedexam.mainapp.dto.email.EmailRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "email-service",
        url = "${EMAIL_SERVICE_URL}"
)
public interface EmailClient {

    @PostMapping("/send-reset-email")
    void sendResetEmail(@RequestBody EmailRequest request);
}