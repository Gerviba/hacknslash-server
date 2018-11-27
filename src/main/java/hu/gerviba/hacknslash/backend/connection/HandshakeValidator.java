package hu.gerviba.hacknslash.backend.connection;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

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
import hu.gerviba.hacknslash.backend.services.CustomLoggingService;
import hu.gerviba.hacknslash.backend.services.UserStorageService;
import lombok.extern.slf4j.Slf4j;

/**
 * Handshake validator component
 * @author gerviba
 *
 */
@Slf4j
@Profile(ConfigProfile.GAME_SERVER)
@Component
public class HandshakeValidator implements HandshakeInterceptor {

    private static final String CLIENT_SIDE_SESSION_ID_ATTRIBUTE = "hns-session-id";
    public static final String SESSION_ID_ATTRIBUTE = "session-id";

    private static AtomicLong ENTITY_ID_AUTO_INCREMENT = new AtomicLong(0);
    
    @Autowired
    RestTemplate restTemplate;
    
    @Value("${game.auth-server-url:http://127.0.0.1:5000}")
    String authServerUrl;
    
    @Autowired
    UserStorageService users;

    @Autowired
    PlayerRepository players;
    
    @Autowired
    CustomLoggingService logger;
    
    /**
     * Handshake handler
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
            WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            
            if (servletRequest.getHeaders().get(CLIENT_SIDE_SESSION_ID_ATTRIBUTE) == null 
                    || servletRequest.getHeaders().get(CLIENT_SIDE_SESSION_ID_ATTRIBUTE).isEmpty()) {
                log.info("Session id not present " + servletRequest.getRemoteAddress().getAddress().toString());
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false;
            }
            
            String sessionId = servletRequest.getHeaders().get(CLIENT_SIDE_SESSION_ID_ATTRIBUTE).get(0);
            
            ValidationResponse validation = restTemplate.postForObject(authServerUrl + "/auth/validate", 
                    new ValidationRequest(sessionId), ValidationResponse.class);
            
            if (validation.getStatus().getStatus().equals(AuthStatus.VALID.name())) {
                attributes.put(SESSION_ID_ATTRIBUTE, sessionId);
                
                Optional<PlayerEntity> player = players.findById(validation.getUser().getUuid());
                
                PlayerEntity entity = player.isPresent() 
                        ? player.get() 
                        : new PlayerEntity(validation.getUser().getUuid());
                entity.setEntityId(ENTITY_ID_AUTO_INCREMENT.getAndIncrement());
                entity.setName(validation.getUser().getName());
                entity.setSessionId(sessionId);
                entity.setHp(entity.getMaxHp());
                players.save(entity);
                users.addPlayer(sessionId, entity);
                
                log.info("Session validated. User " + entity.getName() + " authenticated.");
                logger.info("A new user " + entity.getName() + " logged in.");
                return true;
            } else {
                log.info("Session invalid from " + request.getRemoteAddress().getHostName());
                logger.warning("Session invalid from " + request.getRemoteAddress().getHostName());
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false;
            }
        }
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return false;
    }

    /**
     * After handshake validated
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception ex) {
        
    }
    
}