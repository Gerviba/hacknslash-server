package hu.gerviba.hacknslash.backend.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.gerviba.hacknslash.backend.ConfigProfile;
import hu.gerviba.hacknslash.backend.packets.ChatMessagePacket;

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
    
    @PostMapping("chat")
    String chatMessageTest(@RequestBody ChatMessagePacket data, HttpServletRequest request) {
        simpMessagingTemplate.convertAndSend("/topic/chat", data);
        return "SENT";
    }
    
}
