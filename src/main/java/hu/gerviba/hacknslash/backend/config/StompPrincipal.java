package hu.gerviba.hacknslash.backend.config;

import java.security.Principal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class StompPrincipal implements Principal {

    @Getter
    private final String name;
    
}
