package hu.gerviba.hacknslash.backend.connection;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import hu.gerviba.hacknslash.backend.ConfigProfile;
import hu.gerviba.hacknslash.backend.model.PlayerEntity;
import hu.gerviba.hacknslash.backend.pojo.auth.AuthStatus;
import hu.gerviba.hacknslash.backend.pojo.auth.ValidationRequest;
import hu.gerviba.hacknslash.backend.pojo.auth.ValidationResponse;
import hu.gerviba.hacknslash.backend.repos.PlayerRepository;
import hu.gerviba.hacknslash.backend.services.UserStorageService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile(ConfigProfile.GAME_SERVER)
@Component
public class HandshakeValidator implements HandshakeInterceptor {

    public static final String SESSION_ID_ATTRIBUTE = "session-id";

    @Autowired
    RestTemplate restTemplate;
    
    @Value("${game.auth-server-url:http://127.0.0.1:5000}")
    String authServerUrl;
    
    @Autowired
    UserStorageService users;

    @Autowired
    PlayerRepository players;
    
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
            WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            
            if (servletRequest.getHeaders().get("hns-session-id") == null 
                    || servletRequest.getHeaders().get("hns-session-id").isEmpty()) {
                log.info("Session id not present " + servletRequest.getRemoteAddress().getAddress().toString());
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false;
            }
            
            String sessionId = servletRequest.getHeaders().get("hns-session-id").get(0);
            
            // FIXME: Ez így IO blocking:
            ValidationResponse validation = restTemplate.postForObject(authServerUrl + "/auth/validate", 
                    new ValidationRequest(sessionId), ValidationResponse.class);
            
            if (validation.getStatus().getStatus().equals(AuthStatus.VALID.name())) {
                attributes.put(SESSION_ID_ATTRIBUTE, sessionId);
                
                // TODO: Ne léphessenek be egy sessionID-vel többen
                
                Optional<PlayerEntity> player = players.findById(validation.getUser().getUuid());
                if (!player.isPresent()) {
                    PlayerEntity entity = new PlayerEntity(validation.getUser().getUuid());
                    entity.setName(validation.getUser().getName());
                    players.save(entity);
                    users.addPlayer(sessionId, entity);
                } else {
                    users.addPlayer(sessionId, player.get());
                }
                log.info("Session validated. User authenticated.");
                return true;
            } else {
                log.info("Session invalid.");
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false;
            }
        }
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception ex) {
        log.debug("AFTER HANDSHAKE");
        
    }
}