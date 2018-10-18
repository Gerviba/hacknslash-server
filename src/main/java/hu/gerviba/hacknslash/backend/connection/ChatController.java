package hu.gerviba.hacknslash.backend.connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import hu.gerviba.hacknslash.backend.ConfigProfile;
import hu.gerviba.hacknslash.backend.packets.ChatMessagePacket;
import hu.gerviba.hacknslash.backend.services.UserStorageService;

@Profile(ConfigProfile.GAME_SERVER)
@Controller
public class ChatController {

    @Autowired
    SimpMessagingTemplate messaging;
    
    @Autowired
    UserStorageService users;

    @MessageMapping("/chat")
    @SendTo("/topic/chat")
    ChatMessagePacket sendMessage(@Payload ChatMessagePacket chatMessage) {
        System.out.println(chatMessage);
        chatMessage.setType(ChatMessagePacket.MessageType.CHAT);
        chatMessage.setMessage(chatMessage.getSender() + ": " + chatMessage.getMessage());
        return chatMessage;
    }

}
