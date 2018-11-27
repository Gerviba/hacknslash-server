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

@Profile(ConfigProfile.GAME_SERVER)
@Service
public class GlobalPacketService {

    @Autowired
    SimpMessagingTemplate outgoing;
    
    @Autowired
    UserStorageService users;
    
    public void sendSelfUpdate(PlayerEntity pe) {
        outgoing.convertAndSendToUser(pe.getSessionId(), "/topic/self", new SelfInfoUpdatePacket(pe));
    }
    
    public void sendPrivateMessage(PlayerEntity pe, String message) {
        sendPrivateMessage(pe, MessageType.ANNOUNCEMENT, message);
    }
    
    public void sendPrivateMessage(PlayerEntity pe, MessageType type, String message) {
        outgoing.convertAndSendToUser(pe.getSessionId(), "/topic/chat", 
                new ChatMessagePacket(type, "server", "private", message));
    }

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

    public void sendMobDamageTo(PlayerEntity pe, double x, double y, int damage) {
        users.getMap(pe.getMap()).getMobs().stream()
            .filter(mob -> mob.inDistance(x, y, 0.4))
            .forEach(mob -> mob.damage(damage));
    }
    
}
