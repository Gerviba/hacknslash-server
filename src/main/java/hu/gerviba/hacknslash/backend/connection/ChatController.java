package hu.gerviba.hacknslash.backend.connection;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
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
    
    @SubscribeMapping
    void chatChannelSubscript(Principal principal) {
//        principal.
    }
    
    // @SendToUser
    
    
    
}
