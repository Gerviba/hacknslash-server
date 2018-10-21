package hu.gerviba.hacknslash.backend.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import hu.gerviba.hacknslash.backend.model.PlayerEntity;
import hu.gerviba.hacknslash.backend.packets.ChatMessagePacket;
import hu.gerviba.hacknslash.backend.packets.ChatMessagePacket.MessageType;

public class PlayerCommandListener {

    @Autowired
    SimpMessagingTemplate outgoing;
    
    @CommandHandler("ping")
    void ping(PlayerEntity player, String[] args) {
        outgoing.convertAndSendToUser(player.getSessionId(), "/topic/chat", 
                new ChatMessagePacket(MessageType.ANNOUNCEMENT, "PING-CMD", "ALL", "Pong!"));
    }
    
    @CommandHandler("coords")
    void coords(PlayerEntity player, String[] args) {
        outgoing.convertAndSendToUser(player.getSessionId(), "/topic/chat", 
                new ChatMessagePacket(MessageType.ANNOUNCEMENT, "PING-CMD", "ALL", 
                        player.getX() + ", " + player.getY()));
    }
    
}
