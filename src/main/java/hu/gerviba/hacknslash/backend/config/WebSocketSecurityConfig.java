package hu.gerviba.hacknslash.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

import hu.gerviba.hacknslash.backend.ConfigProfile;

/**
 * WebScoket security config class
 * @author Gergely Szabó
 */
@Profile(ConfigProfile.GAME_SERVER)
@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    /**
     * Config permissions for channels
     */
    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                .simpMessageDestMatchers(
                        "/topic/chat", 
                        "/topic/action", 
                        "/topic/map", 
                        "/topic/telemetry", 
                        "/topic/skills",
                        "/topic/self"
                        ).denyAll()
                .anyMessage().permitAll();
    }
    
    /**
     * Config message broker
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
    }

    /**
     * Allow from all origins
     */
    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}