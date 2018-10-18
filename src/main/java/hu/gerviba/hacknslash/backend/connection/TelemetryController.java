package hu.gerviba.hacknslash.backend.connection;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import hu.gerviba.hacknslash.backend.ConfigProfile;
import hu.gerviba.hacknslash.backend.packets.TelemetryPacket;
import hu.gerviba.hacknslash.backend.packets.TelemetryPacket.PlayerModelStatus;
import hu.gerviba.hacknslash.backend.packets.TelemetryUpdatePacket;
import hu.gerviba.hacknslash.backend.services.UserStorageService;

@Profile(ConfigProfile.GAME_SERVER)
@Controller
@EnableScheduling
public class TelemetryController {

    @Autowired
    SimpMessagingTemplate messaging;
    
    @Autowired
    UserStorageService users;
    
    @MessageMapping("/telemetry")
    void sendTelemetry(@Payload TelemetryUpdatePacket telemetry, SimpMessageHeaderAccessor header) {
        users.getPlayer((String) header.getSessionAttributes().get(HandshakeValidator.SESSION_ID_ATTRIBUTE))
                .update(telemetry);
    }
    
    @Scheduled(fixedRate = 50)
    void updateTelemetry() {
        TelemetryPacket packet = new TelemetryPacket();
        packet.setPlayers(users.getAll().stream()
                .map(user -> new PlayerModelStatus(user))
                .collect(Collectors.toList()));
        users.getAll().forEach(user -> messaging.convertAndSend("/topic/telemetry", packet));
    }
}
