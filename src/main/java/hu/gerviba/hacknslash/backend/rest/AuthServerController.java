package hu.gerviba.hacknslash.backend.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.gerviba.hacknslash.backend.ConfigProfile;
import hu.gerviba.hacknslash.backend.pojo.auth.LoginRequest;
import hu.gerviba.hacknslash.backend.pojo.auth.LoginResponse;
import hu.gerviba.hacknslash.backend.pojo.auth.RegisterRequest;
import hu.gerviba.hacknslash.backend.pojo.auth.RegistrationResponse;
import hu.gerviba.hacknslash.backend.pojo.auth.ValidationRequest;
import hu.gerviba.hacknslash.backend.pojo.auth.ValidationResponse;
import hu.gerviba.hacknslash.backend.services.AuthService;

/**
 * Auth Server Rest controller
 * @author Gergely Szabó
 */
@Profile(ConfigProfile.AUTH_SERVER)
@RestController
@RequestMapping("/auth")
public class AuthServerController {
    
    @Autowired
    AuthService auth;
    
    /**
     * The <pre>/auth/login</pre> endpoint
     * @param data Login request data
     * @param request Raw servlet request
     * @return Login response handled by {@link AuthService#authenticate(String, String, String)}
     */
    @PostMapping("/login")
    LoginResponse login(@RequestBody LoginRequest data, HttpServletRequest request) {
        return auth.authenticate(data.getEmail(), data.getPassword(), request.getRemoteAddr());
    }
    
    /**
     * The <pre>/auth/validate</pre> endpoint
     * @param data Session validation request data
     * @param request Raw servlet request
     * @return Validation response handled by {@link AuthService#checkSession(String, String)}
     */
    @PostMapping("/validate")
    ValidationResponse validate(@RequestBody ValidationRequest data, HttpServletRequest request) {
        return auth.checkSession(data.getSessionId(), request.getRemoteAddr());
    }
    
    /**
     * The <pre>/auth/register</pre> endpoint
     * @param data Register request data
     * @return Registration response handled by {@link AuthService#register(String, String, String)}
     */
    @PostMapping("/register")
    RegistrationResponse register(@RequestBody RegisterRequest data) {
        return auth.register(data.getUsername(), data.getEmail(), data.getPassword());
    }
    
}
