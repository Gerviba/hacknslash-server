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
import hu.gerviba.hacknslash.backend.pojo.auth.ValidateRequest;
import hu.gerviba.hacknslash.backend.pojo.auth.ValidationResponse;
import hu.gerviba.hacknslash.backend.services.AuthService;

@Profile(ConfigProfile.AUTH_SERVER)
@RestController
@RequestMapping("/auth")
public class AuthServerController {
    
    @Autowired
    AuthService auth;
    
    @PostMapping("/login")
    LoginResponse login(@RequestBody LoginRequest data, HttpServletRequest request) {
        return auth.authenticate(data.getEmail(), data.getPassword(), request.getRemoteAddr());
    }
    
    @PostMapping("/validate")
    ValidationResponse validate(@RequestBody ValidateRequest data, HttpServletRequest request) {
        return auth.checkSession(data.getSessionId(), request.getRemoteAddr());
    }
    
    @PostMapping("/register")
    RegistrationResponse register(@RequestBody RegisterRequest data) {
        return auth.register(data.getUsername(), data.getEmail(), data.getPassword());
    }
    
}
