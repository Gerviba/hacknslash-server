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

/**
 * Self info request controller
 * @author Gergely Szabó
 */
@Profile(ConfigProfile.GAME_SERVER)
@Controller
public class InformationController {
    
    @Autowired
    SimpMessagingTemplate messaging;
    
    @Autowired
    UserStorageService users;

    /**
     * User requested an info update
     * @param header SIMP header
     * @return SelfInfo packet (with hp, exp, mana, level, money)
     */
    @MessageMapping("/self")
    @SendToUser("/topic/self")
    SelfInfoUpdatePacket sendMessage(SimpMessageHeaderAccessor header) {
        PlayerEntity pe = users.getPlayer((String) header.getSessionAttributes()
                .get(HandshakeValidator.SESSION_ID_ATTRIBUTE));
        pe.updateAppearance();
        return new SelfInfoUpdatePacket(pe);
    }
    
}
