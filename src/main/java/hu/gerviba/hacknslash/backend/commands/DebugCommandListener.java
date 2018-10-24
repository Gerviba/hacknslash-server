package hu.gerviba.hacknslash.backend.commands;

import org.springframework.beans.factory.annotation.Autowired;

import hu.gerviba.hacknslash.backend.model.PlayerEntity;
import hu.gerviba.hacknslash.backend.services.GlobalPacketService;

public class DebugCommandListener {

    @Autowired
    GlobalPacketService packets;
    
    @CommandHandler("damage")
    void ping(PlayerEntity player, String[] args) {
        player.setHp(player.getHp() - 10);
        packets.sendSelfUpdate(player);
    }
    
}
