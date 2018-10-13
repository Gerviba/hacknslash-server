package hu.gerviba.hacknslash.backend.packets;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class ChatMessagePacket {

    public static enum MessageType {
        CHAT,
        PARTY,
        CLAN,
        WHISPER,
        ANNOUNCEMENT,
        JOIN,
        LEAVE,
        ;
    }

    @Getter
    @Setter
    MessageType type;
    
    @Getter
    @Setter
    String sender;

    @Getter
    @Setter
    String target;
    
    @Getter
    @Setter
    String message;

    public ChatMessagePacket() {
    }
    
    public ChatMessagePacket(MessageType type, String sender, String target, String message) {
        this.type = type;
        this.sender = sender;
        this.target = target;
        this.message = message;
    }
    
}
