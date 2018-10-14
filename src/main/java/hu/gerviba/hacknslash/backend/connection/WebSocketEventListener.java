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

import hu.gerviba.hacknslash.backend.ConfigProfile;
import hu.gerviba.hacknslash.backend.packets.ChatMessagePacket;
import hu.gerviba.hacknslash.backend.services.UserStorageService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile(ConfigProfile.GAME_SERVER)
@Component
public class WebSocketEventListener {

    @Autowired
    SimpMessageSendingOperations messagingTemplate;

    @Autowired
    UserStorageService users;
    
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        log.info("Connecting: " + event.getMessage());
    }
    
    @EventListener
    public void handleWebSocketConnectedListener(SessionConnectedEvent event) {
        log.info("Received a new web socket connection: " + event.getMessage());
    }
    
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        log.info("Disconnected " + headerAccessor.getSessionAttributes().get(HandshakeValidator.SESSION_ID_ATTRIBUTE));
        
        users.removePlayer((String) headerAccessor.getSessionAttributes().get(HandshakeValidator.SESSION_ID_ATTRIBUTE));
        
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if(username != null) {
            log.info("User Disconnected : " + username);

            ChatMessagePacket chatMessage = new ChatMessagePacket();
            chatMessage.setType(ChatMessagePacket.MessageType.LEAVE);
            chatMessage.setSender(username);

            messagingTemplate.convertAndSend("/topic/chat", chatMessage);
        }
    }
}