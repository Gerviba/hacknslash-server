package hu.gerviba.hacknslash.backend.packets;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatMessagePacket {

    public static enum MessageType {
        CHAT,
        PARTY,
        CLAN,
        WHISPER,
        ANNOUNCEMENT,
        JOIN,
        LEAVE,
        ACTION,
        WARNING,
        SERVER
    }

    private MessageType type;
    private String sender;
    private String target;
    private String message;
    
}
