package hu.gerviba.hacknslash.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

import hu.gerviba.hacknslash.backend.ConfigProfile;

@Profile(ConfigProfile.GAME_SERVER)
@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                // users cannot send to these broker destinations, only the application can
                .simpMessageDestMatchers(
                        "/topic/chat", 
                        "/topic/action", 
                        "/topic/map", 
                        "/topic/telemetry", 
                        "/topic/skills"
                        ).denyAll()
                //.anyMessage().authenticated();
                .anyMessage().permitAll();
    }
    
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}