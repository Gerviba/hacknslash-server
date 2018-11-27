package hu.gerviba.hacknslash.backend.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Pojo to store server list information
 * @author Gergely Szabó
 */
@Data
@AllArgsConstructor
public class ServerListInfoResponse {

    private String name;
    private String motd;
    private int users;
    private int maxUsers;
    private String icon;
    
}
