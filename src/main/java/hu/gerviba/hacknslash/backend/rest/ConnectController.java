package hu.gerviba.hacknslash.backend.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.gerviba.hacknslash.backend.ConfigProfile;
import hu.gerviba.hacknslash.backend.pojo.ServerListInfoResponse;

@Profile(ConfigProfile.GAME_SERVER)
@RestController
@RequestMapping("/api/connect/")
public class ConnectController {

    @Value("${game.name:Unnamed Server}")
    String name;
    
    @Value("${game.motd:Motd of the server}")
    String motd;
    
    @Value("${game.max-users:16}")
    int maxUsers;
    
    @Value("${game.icon}")
    String icon;
    
    @GetMapping("/info")
    ServerListInfoResponse info() {
        return new ServerListInfoResponse(name, motd, 0, maxUsers, icon);
    }
    
}
