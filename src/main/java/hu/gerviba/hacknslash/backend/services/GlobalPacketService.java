package hu.gerviba.hacknslash.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import hu.gerviba.hacknslash.backend.ConfigProfile;
import hu.gerviba.hacknslash.backend.model.PlayerEntity;
import hu.gerviba.hacknslash.backend.packets.SelfInfoUpdatePacket;

@Profile(ConfigProfile.GAME_SERVER)
@Service
public class GlobalPacketService {

    @Autowired
    SimpMessagingTemplate outgoing;
    
    public void sendSelfUpdate(PlayerEntity pe) {
        outgoing.convertAndSendToUser(pe.getSessionId(), "/topic/self", new SelfInfoUpdatePacket(pe));
    }
    
}
