package hu.gerviba.hacknslash.backend.pojo.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Pojo to store login request information
 * @author Gergely Szabó
 */
@Data
@AllArgsConstructor
public class LoginRequest {

    private String email;
    private String password;
    
}
