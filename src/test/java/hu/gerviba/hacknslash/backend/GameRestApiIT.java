package hu.gerviba.hacknslash.backend;

import static org.junit.Assert.assertNotEquals;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import hu.gerviba.hacknslash.backend.pojo.ServerListInfoResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { HacknslashApplication.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
public class GameRestApiIT {

    @LocalServerPort
    int port;

    @Autowired
    RestTemplate rest;

    @Test
    @DisplayName("Server list")
    public void testRegister() {
        ServerListInfoResponse slr = rest.getForObject("http://localhost:" + port + "/api/connect/info",
                ServerListInfoResponse.class);
        
        assertNotEquals(null, slr.getName());
        assertNotEquals("", slr.getName());
        assertNotEquals(null, slr.getMotd());
        assertNotEquals("", slr.getMotd());
        assertNotEquals(null, slr.getIcon());
        assertNotEquals("", slr.getIcon());
    }

}
