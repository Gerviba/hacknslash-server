package hu.gerviba.hacknslash.backend.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.gerviba.hacknslash.backend.ConfigProfile;
import hu.gerviba.hacknslash.backend.packets.ChatMessagePacket;
import hu.gerviba.hacknslash.backend.packets.MapLoadPacket;
import hu.gerviba.hacknslash.backend.services.UserStorageService;

@Profile(ConfigProfile.GAME_SERVER)
@RestController
@RequestMapping("/api/admin/")
public class AdminController {

    // Require admin token
    // GET user list
    // GET user count
    // GET map list
    // POST message
    // POST teleport
    // POST kick user
    
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    UserStorageService users;
    
    @PostMapping("chat")
    String chatMessageTest(@RequestBody ChatMessagePacket data, HttpServletRequest request) {
        simpMessagingTemplate.convertAndSend("/topic/chat", data);
        return "SENT";
    }

    @GetMapping("map/{name}")
    MapLoadPacket mapPacketTest(@PathVariable String name, HttpServletRequest request) {
        return users.getMap(name).getMapLoadPacket();
    }

    @GetMapping("sendmap/{name}")
    String sendMapTest(@PathVariable String name, HttpServletRequest request) {
        simpMessagingTemplate.convertAndSend("/topic/map", users.getMap(name).getMapLoadPacket());
        return "DONE";
    }
    
}
