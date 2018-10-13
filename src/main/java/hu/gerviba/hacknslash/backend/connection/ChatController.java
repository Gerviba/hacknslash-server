package hu.gerviba.hacknslash.backend.connection;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import hu.gerviba.hacknslash.backend.packets.ChatMessagePacket;

@Controller
public class ChatController {

    @MessageMapping("/chat")
    @SendTo("/topic/chat")
    ChatMessagePacket sendMessage(@Payload ChatMessagePacket chatMessage) {
        System.out.println(chatMessage);
        chatMessage.setType(ChatMessagePacket.MessageType.CHAT);
        return chatMessage;
    }
    
}
