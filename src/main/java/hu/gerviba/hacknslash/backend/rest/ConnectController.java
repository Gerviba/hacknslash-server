package hu.gerviba.hacknslash.backend.rest;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.gerviba.hacknslash.backend.ConfigProfile;
import hu.gerviba.hacknslash.backend.pojo.ServerListInfoResponse;
import hu.gerviba.hacknslash.backend.services.UserStorageService;

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
    
    @Value("${game.icon-location:resources/icon.png}")
    String iconLocation;
    
    String icon;
    
    @Autowired
    UserStorageService users;
    
    @PostConstruct
    void init() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedImage image = ImageIO.read(new File(iconLocation)); 
        ImageIO.write(image, "png", baos);
        icon = Base64.getEncoder().encodeToString(baos.toByteArray());
        baos.close();
    }
    
    @GetMapping("/info")
    ServerListInfoResponse info() {
        return new ServerListInfoResponse(name, motd, users.countPlayers(), maxUsers, icon);
    }
    
}
