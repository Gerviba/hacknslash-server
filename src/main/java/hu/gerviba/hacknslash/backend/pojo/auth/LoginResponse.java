package hu.gerviba.hacknslash.backend.pojo.auth;

import hu.gerviba.hacknslash.backend.pojo.SimpleStatusResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Pojo to store login response data
 * @author Gergely Szabó
 */
@Data
@AllArgsConstructor
public class LoginResponse {
    
    private SimpleStatusResponse status;
    private UserRepresentation user;
    private String[] servers;
        
}
