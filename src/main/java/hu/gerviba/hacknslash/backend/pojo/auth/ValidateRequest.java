package hu.gerviba.hacknslash.backend.pojo.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ValidateRequest {

    @Getter
    @Setter
    private String sessionId;
    
}
