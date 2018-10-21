package hu.gerviba.hacknslash.backend.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import hu.gerviba.hacknslash.backend.model.PlayerEntity;
import hu.gerviba.hacknslash.backend.packets.SelfInfoUpdatePacket;

public class DebugCommandListener {

    @Autowired
    SimpMessagingTemplate outgoing;
    
    @CommandHandler("damage")
    void ping(PlayerEntity player, String[] args) {
        player.setHp(player.getHp() - 10);
        outgoing.convertAndSendToUser(player.getSessionId(), "/topic/self", new SelfInfoUpdatePacket(player));
    }
    
}
