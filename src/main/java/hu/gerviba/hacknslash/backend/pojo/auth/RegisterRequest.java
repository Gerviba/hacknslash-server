package hu.gerviba.hacknslash.backend.pojo.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Pojo to store register request data
 * @author Gergely Szabó
 */
@Data
@AllArgsConstructor
public class RegisterRequest {

    private String username;
    private String email;
    private String password;
    
}
