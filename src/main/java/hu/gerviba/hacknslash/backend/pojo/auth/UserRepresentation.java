package hu.gerviba.hacknslash.backend.pojo.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Pojo to store user information
 * @author Gergely Szabó
 */
@Data
@AllArgsConstructor
public class UserRepresentation {

    private String uuid;
    private String name;
    private String sessionId;
    
}