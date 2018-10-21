package hu.gerviba.hacknslash.backend.connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import hu.gerviba.hacknslash.backend.ConfigProfile;
import hu.gerviba.hacknslash.backend.model.PlayerEntity;
import hu.gerviba.hacknslash.backend.packets.ChatMessagePacket;
import hu.gerviba.hacknslash.backend.packets.ChatMessagePacket.MessageType;
import hu.gerviba.hacknslash.backend.services.UserStorageService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile(ConfigProfile.GAME_SERVER)
@Component
public class WebSocketEventListener {

    @Autowired
    SimpMessageSendingOperations messaging;
    
    @Autowired
    UserStorageService users;
    
    @EventListener
    public void handleConnect(SessionConnectEvent event) {
        log.info("Connecting: " + event.getMessage());
    }
    
    @EventListener
    public void handleConnected(SessionConnectedEvent event) {
        int start = (event.getMessage().toString()).indexOf("{session-id=");
        String sessionId = (event.getMessage().toString())
                .substring(start + 12, (event.getMessage().toString()).indexOf("}", start));
        log.info("Received a new web socket connection: " + sessionId);
        PlayerEntity player = users.getPlayer(sessionId);
        messaging.convertAndSend("/topic/chat", 
                new ChatMessagePacket(MessageType.JOIN, "SERVER", "ALL", player.getName() + " joined the server"));
    }
    
    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        log.info("Disconnected " + headerAccessor.getSessionAttributes().get(HandshakeValidator.SESSION_ID_ATTRIBUTE));
        PlayerEntity player = users.getPlayer((String) headerAccessor
                .getSessionAttributes().get(HandshakeValidator.SESSION_ID_ATTRIBUTE));
        messaging.convertAndSend("/topic/chat", 
                new ChatMessagePacket(MessageType.LEAVE, "SERVER", "ALL", player.getName() + " left the server"));
        users.removePlayer((String) headerAccessor.getSessionAttributes().get(HandshakeValidator.SESSION_ID_ATTRIBUTE));
    }
    
    @EventListener
    public void handleSubscribe(SessionSubscribeEvent event) {
        log.info("Subscribe: " + event.getMessage());
    }

}