package hu.gerviba.hacknslash.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import hu.gerviba.hacknslash.backend.packets.ChatMessagePacket;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WebSocketEventListener {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        System.out.println("SessionId = " + event.getMessage().getHeaders().get("hns-session-id"));
        log.info("Connecting: " + event.getMessage());
    }
    
    @EventListener
    public void handleWebSocketConnectedListener(SessionConnectedEvent event) {
        log.info("Received a new web socket connection: " + event.getMessage());
    }
    
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

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