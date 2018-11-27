package hu.gerviba.hacknslash.backend;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import hu.gerviba.hacknslash.backend.pojo.auth.AuthStatus;
import hu.gerviba.hacknslash.backend.pojo.auth.LoginRequest;
import hu.gerviba.hacknslash.backend.pojo.auth.LoginResponse;
import hu.gerviba.hacknslash.backend.pojo.auth.RegisterRequest;
import hu.gerviba.hacknslash.backend.pojo.auth.RegistrationResponse;
import hu.gerviba.hacknslash.backend.pojo.auth.ValidationRequest;
import hu.gerviba.hacknslash.backend.pojo.auth.ValidationResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { HacknslashApplication.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
public class AuthRestApiIT {

    @LocalServerPort
    int port;

    @Autowired
    RestTemplate rest;

    @Test
    @DisplayName("Register user")
    public void testRegister() {
        RegistrationResponse rr = rest.postForObject("http://localhost:" + port + "/auth/register",
                new RegisterRequest("testuser", "test@test.tdl", "testpassword"),
                RegistrationResponse.class);
        assertEquals(AuthStatus.VALID.name(), rr.getStatus().getStatus());

        rr = rest.postForObject("http://localhost:" + port + "/auth/register",
                new RegisterRequest("testuser", "test2@test.tdl", "testpassword"),
                RegistrationResponse.class);
        assertEquals(AuthStatus.INVALID.name(), rr.getStatus().getStatus());
        assertEquals(rr.getStatus().getMessage(), "USERNAME_IN_USE");

        rr = rest.postForObject("http://localhost:" + port + "/auth/register",
                new RegisterRequest("testuser3", "test@test.tdl", "testpassword"),
                RegistrationResponse.class);
        assertEquals(AuthStatus.INVALID.name(), rr.getStatus().getStatus());
        assertEquals(rr.getStatus().getMessage(), "EMAIL_IN_USE");

    }

    @Test
    @DisplayName("Login user")
    public void testLogin() {
        RegistrationResponse rr = rest.postForObject("http://localhost:" + port + "/auth/register",
                new RegisterRequest("testuserlogin", "testuserlogin@test.tdl", "testpassword2"),
                RegistrationResponse.class);
        assertEquals(AuthStatus.VALID.name(), rr.getStatus().getStatus());

        LoginResponse lr = rest.postForObject("http://localhost:" + port + "/auth/login",
                new LoginRequest("testuserlogin@test.tdl", "testpassword2"),
                LoginResponse.class);
        assertEquals(AuthStatus.VALID.name(), lr.getStatus().getStatus());
        assertEquals(lr.getUser().getName(), "testuserlogin");
        
        lr = rest.postForObject("http://localhost:" + port + "/auth/login",
                new LoginRequest("testuserlogin@test.tdl", "testpassword3"),
                LoginResponse.class);
        assertEquals(AuthStatus.INVALID.name(), lr.getStatus().getStatus());
        assertEquals(lr.getStatus().getMessage(), "INVALID_CREDENTIALS");
        
        lr = rest.postForObject("http://localhost:" + port + "/auth/login",
                new LoginRequest("testuserlogininvalid@test.tdl", "testpassword2"),
                LoginResponse.class);
        assertEquals(AuthStatus.INVALID.name(), lr.getStatus().getStatus());
        assertEquals(lr.getStatus().getMessage(), "NOT_REGISTERED");

    }
    
    @Test
    @DisplayName("Check session")
    public void testSessionValidation() {
        RegistrationResponse rr = rest.postForObject("http://localhost:" + port + "/auth/register",
                new RegisterRequest("testusercheck", "testusercheck@test.tdl", "testpassword2"),
                RegistrationResponse.class);
        assertEquals(AuthStatus.VALID.name(), rr.getStatus().getStatus());

        LoginResponse lr = rest.postForObject("http://localhost:" + port + "/auth/login",
                new LoginRequest("testusercheck@test.tdl", "testpassword2"),
                LoginResponse.class);
        assertEquals(AuthStatus.VALID.name(), lr.getStatus().getStatus());
        String session = lr.getUser().getSessionId();
        assertNotEquals(null, session);
        assertNotEquals("", session);
        
        ValidationResponse vr = rest.postForObject("http://localhost:" + port + "/auth/validate",
                new ValidationRequest(session),
                ValidationResponse.class);
        assertEquals(AuthStatus.VALID.name(), vr.getStatus().getStatus());
        assertEquals(vr.getUser().getName(), "testusercheck");
        
        vr = rest.postForObject("http://localhost:" + port + "/auth/validate",
                new ValidationRequest("sajt"),
                ValidationResponse.class);
        assertEquals(AuthStatus.INVALID.name(), vr.getStatus().getStatus());
    }

}
