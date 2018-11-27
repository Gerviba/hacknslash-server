package hu.gerviba.hacknslash.backend.connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import hu.gerviba.hacknslash.backend.ConfigProfile;
import hu.gerviba.hacknslash.backend.model.PlayerEntity;
import hu.gerviba.hacknslash.backend.packets.JustConnectedPacket;
import hu.gerviba.hacknslash.backend.packets.MapLoadPacket;
import hu.gerviba.hacknslash.backend.packets.TelemetryUpdatePacket;
import hu.gerviba.hacknslash.backend.pojo.game.IngameMap;
import hu.gerviba.hacknslash.backend.services.GlobalPacketService;
import hu.gerviba.hacknslash.backend.services.UserStorageService;

/**
 * Player position related controller
 * @author Gergely Szabó
 */
@Profile(ConfigProfile.GAME_SERVER)
@Controller
@EnableScheduling
public class TelemetryController {

    @Autowired
    SimpMessagingTemplate messaging;
    
    @Autowired
    UserStorageService users;

    @Autowired
    GlobalPacketService packets;
    
    /**
     * Position update packet
     * @param telemetry Request packet (that holds the player's new coordinates)
     * @param header SIMP header
     */
    @MessageMapping("/telemetry")
    void sendTelemetry(@Payload TelemetryUpdatePacket telemetry, SimpMessageHeaderAccessor header) {
        users.getPlayer((String) header.getSessionAttributes()
                .get(HandshakeValidator.SESSION_ID_ATTRIBUTE))
                .update(telemetry);
    }

    /**
     * Request map load
     * @param connected Request packet
     * @param header SIMP header
     * @return The full map
     */
    @MessageMapping("/connected")
    @SendToUser("/topic/map")
    MapLoadPacket mapConnect(@Payload JustConnectedPacket connected, SimpMessageHeaderAccessor header) {
        PlayerEntity pe = users.getPlayer((String) header.getSessionAttributes()
                .get(HandshakeValidator.SESSION_ID_ATTRIBUTE));
        packets.sendFullInventoryUpdate(pe);
        
        IngameMap map = users.getMap(pe.getMap());
        map.addPlayer(pe);
        return map.getMapLoadPacket();
    }
    
    /**
     * Update timer
     */
    @Scheduled(fixedRate = 50)
    void updateTelemetry() {
        users.getMaps().forEach(map -> map.updateTelemetry(messaging));
    }
    
}
