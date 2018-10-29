package hu.gerviba.hacknslash.backend.commands;

import org.springframework.beans.factory.annotation.Autowired;

import hu.gerviba.hacknslash.backend.model.PlayerEntity;
import hu.gerviba.hacknslash.backend.services.GlobalPacketService;

public class PlayerCommandListener {

    @Autowired
    GlobalPacketService packets;
    
    @CommandHandler("ping")
    void ping(PlayerEntity player, String[] args) {
        packets.sendPrivateMessage(player, "Pong!");
    }
    
    @CommandHandler("coords")
    void coords(PlayerEntity player, String[] args) {
        packets.sendPrivateMessage(player, player.getX() + ", " + player.getY());
    }
    
}
