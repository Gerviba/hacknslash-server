package hu.gerviba.hacknslash.backend.commands;

import org.springframework.beans.factory.annotation.Autowired;

import hu.gerviba.hacknslash.backend.model.PlayerEntity;
import hu.gerviba.hacknslash.backend.packets.ChatMessagePacket.MessageType;
import hu.gerviba.hacknslash.backend.services.GlobalPacketService;

public class DebugCommandListener {

    @Autowired
    GlobalPacketService packets;
    
    @CommandHandler("damage")
    void ping(PlayerEntity player, String[] args) {
        player.setHp(player.getHp() - 10);
        packets.sendSelfUpdate(player);
    }
    
    @CommandHandler("testmsgs")
    void testmsgs(PlayerEntity player, String[] args) {
        packets.sendPrivateMessage(player, MessageType.ANNOUNCEMENT, "TIP: Use 'I' to open inventory");
        packets.sendPrivateMessage(player, MessageType.JOIN, "Tester joined the game");
        packets.sendPrivateMessage(player, MessageType.WARNING, "Command not found");
        packets.sendPrivateMessage(player, MessageType.PARTY, "[PARTY] Tester: Hi!");
        packets.sendPrivateMessage(player, MessageType.CHAT, "Tester: Be right back!");
        packets.sendPrivateMessage(player, MessageType.ACTION, "You found: 50 coins");
        packets.sendPrivateMessage(player, MessageType.SERVER, "Item added to your inventory");
        packets.sendPrivateMessage(player, MessageType.LEAVE, "Tester left the server");
    }
    
}
