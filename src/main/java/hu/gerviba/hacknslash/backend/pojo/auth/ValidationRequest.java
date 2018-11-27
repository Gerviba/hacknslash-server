package hu.gerviba.hacknslash.backend.pojo.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Pojo to store validation request
 * @author Gergely Szabó
 */
@Data
@AllArgsConstructor
public class ValidationRequest {

    private String sessionId;
    
}
