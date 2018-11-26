package hu.gerviba.hacknslash.backend.connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import hu.gerviba.hacknslash.backend.ConfigProfile;
import hu.gerviba.hacknslash.backend.commands.CommandService;
import hu.gerviba.hacknslash.backend.model.PlayerEntity;
import hu.gerviba.hacknslash.backend.packets.ChatMessagePacket;
import hu.gerviba.hacknslash.backend.packets.ChatMessagePacket.MessageType;
import hu.gerviba.hacknslash.backend.services.CustomLoggingService;
import hu.gerviba.hacknslash.backend.services.UserStorageService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile(ConfigProfile.GAME_SERVER)
@Controller
public class ChatController {

    @Autowired
    SimpMessagingTemplate messaging;
    
    @Autowired
    UserStorageService users;

    @Autowired
    CommandService commands;
    
    @Autowired
    CustomLoggingService logger;
    
    @MessageMapping("/chat")
    @SendTo("/topic/chat")
    ChatMessagePacket sendMessage(@Payload ChatMessagePacket chatMessage, SimpMessageHeaderAccessor header) {
        PlayerEntity pe = users.getPlayer((String) header.getSessionAttributes()
                .get(HandshakeValidator.SESSION_ID_ATTRIBUTE));
        

        if (chatMessage.getMessage().startsWith("/")) {
            log.info("Command from " + pe.getName() + ": " + chatMessage.getMessage());
            if (!commands.handle(pe, chatMessage.getMessage())) 
                messaging.convertAndSendToUser(header.getUser().getName(), "/topic/chat", 
                        new ChatMessagePacket(MessageType.WARNING, "", "", "Command not found"));
            return null;
        }

        log.info("Chat message from " + pe.getName() + ": " + chatMessage.getMessage());
        logger.info("Chat message from " + pe.getName() + ": " + chatMessage.getMessage());
        chatMessage.setType(ChatMessagePacket.MessageType.CHAT);
        chatMessage.setMessage(chatMessage.getSender() + ": " + chatMessage.getMessage());
        return chatMessage;
    }

}
