package bg.springadvancedexam.mainapp.config;

import org.springframework.boot.restclient.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.time.Duration;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClientCustomizer restClientCustomizer() {
        return builder -> {
            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

            factory.setConnectTimeout(Duration.ofSeconds(120));

            factory.setReadTimeout(Duration.ofSeconds(120));

            builder.requestFactory(factory);
        };
    }
}
