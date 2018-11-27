package hu.gerviba.hacknslash.backend.commands;

import org.springframework.beans.factory.annotation.Autowired;

import hu.gerviba.hacknslash.backend.model.PlayerEntity;
import hu.gerviba.hacknslash.backend.packets.ChatMessagePacket.MessageType;
import hu.gerviba.hacknslash.backend.services.GlobalPacketService;

/**
 * Debug commands to test the server
 * @author Gergely Szabó
 */
public class DebugCommandListener {

    @Autowired
    GlobalPacketService packets;
    
    /**
     * Hurt the performer player by -10HP
     * @param player Command performer
     * @param args Command arguments
     */
    @CommandHandler("damage")
    void damage(PlayerEntity player, String[] args) {
        player.setHp(player.getHp() - 10);
        packets.sendSelfUpdate(player);
    }
    
    /**
     * Hurt the performer player by -25HP
     * @param player Command performer
     * @param args Command arguments
     */
    @CommandHandler("damage25")
    void damage25(PlayerEntity player, String[] args) {
        player.setHp(player.getHp() - 25);
        packets.sendSelfUpdate(player);
    }
    
    /**
     * Hurt the performer player by -50HP
     * @param player Command performer
     * @param args Command arguments
     */
    @CommandHandler("damage50")
    void damage50(PlayerEntity player, String[] args) {
        player.setHp(player.getHp() - 50);
        packets.sendSelfUpdate(player);
    }
    
    /**
     * Sends the test chat messages to the player
     * @param player Command performer
     * @param args Command arguments
     */
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
