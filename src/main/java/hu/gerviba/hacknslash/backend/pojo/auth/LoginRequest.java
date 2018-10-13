package hu.gerviba.hacknslash.backend.pojo.auth;

import hu.gerviba.hacknslash.backend.pojo.SimpleStatusResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginRequest {

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String password;
    
}
