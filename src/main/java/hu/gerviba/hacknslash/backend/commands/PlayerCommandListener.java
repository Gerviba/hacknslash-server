package hu.gerviba.hacknslash.backend.commands;

import org.springframework.beans.factory.annotation.Autowired;

import hu.gerviba.hacknslash.backend.model.PlayerEntity;
import hu.gerviba.hacknslash.backend.services.GlobalPacketService;

/**
 * Commands for players
 * @author Gergely Szabó
 */
public class PlayerCommandListener {

    @Autowired
    GlobalPacketService packets;
    
    /**
     * Pong!
     * @param player Command performer
     * @param args Command arguments
     */
    @CommandHandler("ping")
    void ping(PlayerEntity player, String[] args) {
        packets.sendPrivateMessage(player, "Pong!");
    }
    
    /**
     * Prints the coordinates of the performer player
     * @param player Command performer
     * @param args Command arguments
     */
    @CommandHandler("coords")
    void coords(PlayerEntity player, String[] args) {
        packets.sendPrivateMessage(player, player.getX() + ", " + player.getY());
    }
    
}
