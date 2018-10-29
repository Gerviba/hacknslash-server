package hu.gerviba.hacknslash.backend.connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import hu.gerviba.hacknslash.backend.ConfigProfile;
import hu.gerviba.hacknslash.backend.model.PlayerEntity;
import hu.gerviba.hacknslash.backend.packets.SelfInfoUpdatePacket;
import hu.gerviba.hacknslash.backend.services.UserStorageService;

@Profile(ConfigProfile.GAME_SERVER)
@Controller
public class InformationController {
    
    @Autowired
    SimpMessagingTemplate messaging;
    
    @Autowired
    UserStorageService users;

    @MessageMapping("/self")
    @SendToUser("/topic/self")
    SelfInfoUpdatePacket sendMessage(SimpMessageHeaderAccessor header) {
        PlayerEntity pe = users.getPlayer((String) header.getSessionAttributes()
                .get(HandshakeValidator.SESSION_ID_ATTRIBUTE));
        pe.updateAppearance();
        return new SelfInfoUpdatePacket(pe);
    }
    
}
