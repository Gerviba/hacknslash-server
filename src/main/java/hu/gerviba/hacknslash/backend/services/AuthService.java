package hu.gerviba.hacknslash.backend.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import hu.gerviba.hacknslash.backend.ConfigProfile;
import hu.gerviba.hacknslash.backend.model.RegisteredUserEntity;
import hu.gerviba.hacknslash.backend.model.SessionUserEntity;
import hu.gerviba.hacknslash.backend.pojo.SimpleStatusResponse;
import hu.gerviba.hacknslash.backend.pojo.auth.AuthStatus;
import hu.gerviba.hacknslash.backend.pojo.auth.LoginResponse;
import hu.gerviba.hacknslash.backend.pojo.auth.RegistrationResponse;
import hu.gerviba.hacknslash.backend.pojo.auth.UserRepresentation;
import hu.gerviba.hacknslash.backend.pojo.auth.ValidationResponse;
import hu.gerviba.hacknslash.backend.repos.SessionRepository;
import hu.gerviba.hacknslash.backend.repos.UserRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * Authenticate service
 * @author Gergely Szabó
 */
@Profile(ConfigProfile.AUTH_SERVER)
@Service
@Transactional
@Slf4j
public class AuthService {
    
    @Autowired
    UserRepository users;
    
    @Autowired
    SessionRepository sessions;
    
    @Autowired
    CommonsUtil util;
    
    @Value("${auth.promoted-servers:[]}")
    String[] promoted;

    @Value("${auth.session-timeout:3600000}")
    long sessionTimeout;
    
    /**
     * Handle the authentication of a user
     * @param email User's email
     * @param password User's password
     * @param ip User's IP address
     * @return Login response
     */
    public LoginResponse authenticate(String email, String password, String ip) {
        log.debug("Checking user auth " + email + " (ip: " + ip + ")");
        Optional<RegisteredUserEntity> user = users.findByEmailIgnoreCase(email);
        if (!user.isPresent())
            return new LoginResponse(
                    new SimpleStatusResponse(AuthStatus.INVALID.name(), "NOT_REGISTERED"), 
                    null, new String[] {});
        
        if (!user.get().getPassword().equalsIgnoreCase(util.hash(password, user.get().getSalt())))
            return new LoginResponse(
                    new SimpleStatusResponse(AuthStatus.INVALID.name(), "INVALID_CREDENTIALS"), 
                    null, new String[] {});
        
        if (user.get().isBanned())
            return new LoginResponse(
                    new SimpleStatusResponse(AuthStatus.INVALID.name(), "BANNED"), 
                    null, new String[] {});
        
        Optional<SessionUserEntity> session = sessions.findById(user.get().getId());
        if (session.isPresent())
            sessions.delete(session.get());
        
        String sessionId = util.generateSessionId(user.get().getId());
        sessions.save(new SessionUserEntity(
                user.get().getId(),
                user.get().getUsername(),
                sessionId,
                ip,
                System.currentTimeMillis()));
        
        log.info("User " + user.get().getUsername() + " authenticated (ip: " + ip + ")");
        return new LoginResponse(new SimpleStatusResponse(AuthStatus.VALID.name(), ""),
                new UserRepresentation(user.get().getId(), user.get().getUsername(), sessionId), 
                promoted);
    }
    
    /**
     * Check session ID before login
     * @param sessionId User's session ID
     * @param serverIp IP address of the server
     * @return Validation response
     */
    public ValidationResponse checkSession(String sessionId, String serverIp) {
        log.debug("Checking session " + sessionId + " for server running on: " + serverIp);
        
        Optional<SessionUserEntity> session = sessions.findBySessionId(sessionId);
        if (!session.isPresent())
            return new ValidationResponse(
                    new SimpleStatusResponse(AuthStatus.INVALID.name(), "INVALID_SESSION"), 
                    null);
        
        if (session.get().getTime() + sessionTimeout < System.currentTimeMillis()) {
            sessions.delete(session.get());
            return new ValidationResponse(
                    new SimpleStatusResponse(AuthStatus.INVALID.name(), "INVALID_SESSION"), 
                    null);
        }
        
        log.info("Session " + sessionId + " is valid for user " 
                + session.get().getUsername() + " (server: " + serverIp + ")");
        return new ValidationResponse(
                new SimpleStatusResponse(AuthStatus.VALID.name(), ""),
                new UserRepresentation(session.get().getId(), session.get().getUsername(), null));
    }

    /**
     * Register a new user
     * @param username Name of the user
     * @param email Email address of the user
     * @param password Password of the user
     * @return Registration response
     */
    public RegistrationResponse register(String username, String email, String password) {
        List<RegisteredUserEntity> errorVector = users.findAllByUsernameIgnoreCaseOrEmailIgnoreCase(
                username, email);
        
        if (errorVector.size() == 0) {
            String salt = util.generateSalt();
            
            RegisteredUserEntity user = new RegisteredUserEntity(null, email, username, 
                    util.hash(password, salt), salt, false);
            
            users.save(user);
            log.info("User registered: " + user.getUsername() + " with id: " + user.getId());
            return new RegistrationResponse(
                    new SimpleStatusResponse(AuthStatus.VALID.name(), ""),
                    new UserRepresentation(user.getId(), user.getUsername(), null));
        }
        
        if (errorVector.get(0).getEmail().equalsIgnoreCase(email))
            return new RegistrationResponse(
                    new SimpleStatusResponse(AuthStatus.INVALID.name(), "EMAIL_IN_USE"),
                    null);
        
        if (errorVector.get(0).getUsername().equalsIgnoreCase(username))
            return new RegistrationResponse(
                    new SimpleStatusResponse(AuthStatus.INVALID.name(), "USERNAME_IN_USE"),
                    null);
        
        return new RegistrationResponse(
                new SimpleStatusResponse(AuthStatus.INVALID.name(), "INTERNAL_ERROR"),
                null);
    }
    
}
