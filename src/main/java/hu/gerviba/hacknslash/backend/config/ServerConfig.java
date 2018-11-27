package hu.gerviba.hacknslash.backend.config;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Server configuration
 * @author Gergely Szabó
 */
@Configuration
public class ServerConfig {

    /**
     * The RestTemplate bean factory
     * @return The {@link RestTemplate} singleton bean
     */
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * The ScheduledExecutorService bean factory
     * @return The {@link ScheduledExecutorService} singleton bean
     */
    @Bean
    ScheduledExecutorService executorService() {
        return Executors.newSingleThreadScheduledExecutor();
    }
    
}
