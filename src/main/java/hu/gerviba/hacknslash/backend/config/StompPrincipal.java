package hu.gerviba.hacknslash.backend.config;

import java.security.Principal;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The STOMP messaging principal
 * @author Gergely Szabó
 */
@AllArgsConstructor
public class StompPrincipal implements Principal {

    @Getter
    private final String name;
    
}
