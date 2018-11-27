package hu.gerviba.hacknslash.backend.services;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import hu.gerviba.hacknslash.backend.ConfigProfile;
import hu.gerviba.hacknslash.backend.model.PlayerEntity;
import hu.gerviba.hacknslash.backend.packets.ChatMessagePacket;
import hu.gerviba.hacknslash.backend.packets.SelfInfoUpdatePacket;
import hu.gerviba.hacknslash.backend.packets.ChatMessagePacket.MessageType;
import hu.gerviba.hacknslash.backend.packets.ItemChangePacket.ChangeMethod;
import hu.gerviba.hacknslash.backend.packets.ItemChangePacket;

/**
 * A service to manage the SimpMessagingTemplate and the packets
 * @author Gergely Szabó
 */
@Profile(ConfigProfile.GAME_SERVER)
@Service
public class GlobalPacketService {

    @Autowired
    SimpMessagingTemplate outgoing;
    
    @Autowired
    UserStorageService users;
    
    /**
     * Sends the SelfUpdate packet.
     * (HP, mana and exp update)
     * @param pe
     */
    public void sendSelfUpdate(PlayerEntity pe) {
        outgoing.convertAndSendToUser(pe.getSessionId(), "/topic/self", new SelfInfoUpdatePacket(pe));
    }
    
    /**
     * Sends private message to player
     * @param pe Player to send the message
     * @param message The message
     */
    public void sendPrivateMessage(PlayerEntity pe, String message) {
        sendPrivateMessage(pe, MessageType.ANNOUNCEMENT, message);
    }
    
    /**
     * Sends private message to player
     * @param pe Player to send the message
     * @param type Type of the message. See: {@link MessageType}
     * @param message The message
     */
    public void sendPrivateMessage(PlayerEntity pe, MessageType type, String message) {
        outgoing.convertAndSendToUser(pe.getSessionId(), "/topic/chat", 
                new ChatMessagePacket(type, "server", "private", message));
    }

    /**
     * Update user inventory.
     * @param pe The user
     */
    public void sendFullInventoryUpdate(PlayerEntity pe) {
        pe.updateAppearance();
        outgoing.convertAndSendToUser(pe.getSessionId(), "/topic/inventory",
                new ItemChangePacket(ChangeMethod.OVERRIDE,
                        pe.getInventory().getItemSlot().entrySet()
                                .stream()
                                .map(slot -> new ItemChangePacket.ChangePart(
                                        slot.getKey(),
                                        slot.getValue().getItemId(),
                                        slot.getValue().getCount()))
                                .collect(Collectors.toList())));
    }

    /**
     * Send damage packets
     * @param cause The damage cause 
     * @param x X coordinate
     * @param y Y coordinate
     * @param damage Damage value
     */
    public void sendMobDamageTo(PlayerEntity cause, double x, double y, int damage) {
        users.getMap(cause.getMap()).getMobs().stream()
            .filter(mob -> mob.inDistance(x, y, 0.6))
            .filter(mob -> mob.damage(damage))
            .forEach(mob -> {
                cause.setExp(cause.getExp() + mob.getExp());
                sendPrivateMessage(cause, MessageType.ACTION, "You received " + mob.getExp() 
                        + " exp for killing " + mob.getName());
            });
        sendSelfUpdate(cause);
    }
    
}
