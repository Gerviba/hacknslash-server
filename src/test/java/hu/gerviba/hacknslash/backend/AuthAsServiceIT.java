package hu.gerviba.hacknslash.backend;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import hu.gerviba.hacknslash.backend.pojo.auth.AuthStatus;
import hu.gerviba.hacknslash.backend.pojo.auth.LoginResponse;
import hu.gerviba.hacknslash.backend.pojo.auth.RegistrationResponse;
import hu.gerviba.hacknslash.backend.pojo.auth.ValidationResponse;
import hu.gerviba.hacknslash.backend.services.AuthService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { HacknslashApplication.class })
public class AuthAsServiceIT {
    
    @Autowired
    AuthService service;
    
    @Test
    @DisplayName("User registration")
    public void testRegister() {
        RegistrationResponse rr = service.register("testuser", "test@test.tdl", "testpassword");
        assertEquals(AuthStatus.VALID.name(), rr.getStatus().getStatus());

        rr = service.register("testuser", "test2@test.tdl", "testpassword");
        assertEquals(AuthStatus.INVALID.name(), rr.getStatus().getStatus());
        assertEquals(rr.getStatus().getMessage(), "USERNAME_IN_USE");

        rr = service.register("testuser3", "test@test.tdl", "testpassword");
        assertEquals(AuthStatus.INVALID.name(), rr.getStatus().getStatus());
        assertEquals(rr.getStatus().getMessage(), "EMAIL_IN_USE");

    }

    @Test
    @DisplayName("Authenticate user")
    public void testLogin() {
        RegistrationResponse rr = service.register("testuserlogin", "testuserlogin@test.tdl", "testpassword2");
        assertEquals(AuthStatus.VALID.name(), rr.getStatus().getStatus());

        LoginResponse lr = service.authenticate("testuserlogin@test.tdl", "testpassword2", "127.0.0.1");
        assertEquals(AuthStatus.VALID.name(), lr.getStatus().getStatus());
        assertEquals(lr.getUser().getName(), "testuserlogin");
        
        lr = service.authenticate("testuserlogin@test.tdl", "testpassword3", "127.0.0.1");
        assertEquals(AuthStatus.INVALID.name(), lr.getStatus().getStatus());
        assertEquals(lr.getStatus().getMessage(), "INVALID_CREDENTIALS");
        
        lr = service.authenticate("testuserlogininvalid@test.tdl", "testpassword2", "127.0.0.1");
        assertEquals(AuthStatus.INVALID.name(), lr.getStatus().getStatus());
        assertEquals(lr.getStatus().getMessage(), "NOT_REGISTERED");

    }
    
    @Test
    @DisplayName("Session validation")
    public void testSessionValidation() {
        RegistrationResponse rr = service.register("testusercheck", "testusercheck@test.tdl", "testpassword2");
        assertEquals(AuthStatus.VALID.name(), rr.getStatus().getStatus());

        LoginResponse lr = service.authenticate("testusercheck@test.tdl", "testpassword2", "127.0.0.1");
        assertEquals(AuthStatus.VALID.name(), lr.getStatus().getStatus());
        String session = lr.getUser().getSessionId();
        assertNotEquals(null, session);
        assertNotEquals("", session);
        
        ValidationResponse vr = service.checkSession(session, "127.0.0.1");
        assertEquals(AuthStatus.VALID.name(), vr.getStatus().getStatus());
        assertEquals(vr.getUser().getName(), "testusercheck");
        
        vr = service.checkSession("sajt", "127.0.0.1");
        assertEquals(AuthStatus.INVALID.name(), vr.getStatus().getStatus());
    }
    
}
