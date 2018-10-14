package hu.gerviba.hacknslash.backend.connection;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import hu.gerviba.hacknslash.backend.packets.ChatMessagePacket;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat")
    @SendTo("/topic/chat")
    ChatMessagePacket sendMessage(@Payload ChatMessagePacket chatMessage) {
        System.out.println(chatMessage);
        chatMessage.setType(ChatMessagePacket.MessageType.CHAT);
        return chatMessage;
    }

    @MessageMapping("/telementry")
    //    @SendTo("/topic/chat")
    ChatMessagePacket sendTelemetry(@Payload ChatMessagePacket chatMessage) {
        System.out.println(chatMessage);
        chatMessage.setType(ChatMessagePacket.MessageType.CHAT);
        return chatMessage;
    }

    @SubscribeMapping("/chat")
    void chatChannelSubscript(@Payload ChatMessagePacket chatMessage, Principal principal) {
        //        principal.
//        simpMessagingTemplate.
        System.out.println(principal.getName());
        System.out.println("Subscribe mapping");
    }

    // @SendToUser

}
