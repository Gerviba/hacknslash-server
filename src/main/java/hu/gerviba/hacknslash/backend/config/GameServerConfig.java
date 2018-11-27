package hu.gerviba.hacknslash.backend.config;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

import hu.gerviba.hacknslash.backend.ConfigProfile;

@Profile(ConfigProfile.GAME_SERVER)
@Configuration
public class GameServerConfig {

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
    @Bean
    ScheduledExecutorService executorService() {
        return Executors.newSingleThreadScheduledExecutor();
    }
    
}
