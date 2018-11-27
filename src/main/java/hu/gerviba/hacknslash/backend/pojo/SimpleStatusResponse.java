package hu.gerviba.hacknslash.backend.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Pojo to store HTTP request status data
 * @author Gergely Szabó
 */
@Data
@AllArgsConstructor
public class SimpleStatusResponse {

    private String status;
    private String message;
    
}
