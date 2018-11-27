package hu.gerviba.hacknslash.backend.commands;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import hu.gerviba.hacknslash.backend.item.ItemType;
import hu.gerviba.hacknslash.backend.item.Items;
import hu.gerviba.hacknslash.backend.model.PlayerEntity;
import hu.gerviba.hacknslash.backend.packets.ChatMessagePacket.MessageType;
import hu.gerviba.hacknslash.backend.services.GlobalPacketService;

/**
 * Admin commands
 * @author Gergely Szabó
 */
public class AdminCommandListener {
    
    @Autowired
    GlobalPacketService packets;
    
    /**
     * Append a starter kit of items to the player
     * @param player Command performer
     * @param args Command arguments
     */
    @CommandHandler("starterkit")
    void starterkit(PlayerEntity player, String[] args) {
        player.getInventory().putItemToSlot(0, Items.WEAPON_BLUE_MAGIC_WAND.getId(), 1);
        player.getInventory().putItemToSlot(1, Items.RING_CARBON.getId(), 1);
        player.getInventory().putItemToSlot(2, Items.HELMET_IRON.getId(), 1);
        player.getInventory().putItemToSlot(3, Items.ARMOR_IRON.getId(), 1);
        player.getInventory().putItemToSlot(4, Items.BOOTS_IRON.getId(), 1);

        player.getInventory().putItemToSlot(10, Items.SKILL_POISON.getId(), 1);
        player.getInventory().putItemToSlot(11, Items.SKILL_WEATER_BEAM.getId(), 1);
        player.getInventory().putItemToSlot(12, Items.SKILL_PURLE_MAGIC.getId(), 1);
        player.getInventory().putItemToSlot(13, Items.SKILL_FLAME_CIRCLE.getId(), 1);
        player.getInventory().putItemToSlot(14, Items.SKILL_STORM.getId(), 1);
        player.getInventory().putItemToSlot(15, Items.POTION_HEALTH_2.getId(), 1);
        player.getInventory().putItemToSlot(16, Items.POTION_MANA_1.getId(), 1);
        
        int id = 22;
        for (ItemType it : Items.ALL.values().stream()
                .sorted((a, b) -> Integer.compare(a.getId(), b.getId()))
                .collect(Collectors.toList()))
            player.getInventory().putItemToSlot(id++, it.getId(), 1);
        
        packets.sendFullInventoryUpdate(player);
    }
    
    /**
     * Add a new from all the existing items
     * @param player Command performer
     * @param args Command arguments
     */
    @CommandHandler("allitems")
    void allitems(PlayerEntity player, String[] args) {
        for (ItemType it : Items.ALL.values().stream()
                .sorted((a, b) -> Integer.compare(a.getId(), b.getId()))
                .collect(Collectors.toList()))
            player.getInventory().addItem(it.getId(), 1);
        
        packets.sendFullInventoryUpdate(player);
    }
    
    /**
     * Add n new from the selected items
     * @param player Command performer
     * @param args Command arguments
     */
    @CommandHandler("item")
    void item(PlayerEntity player, String[] args) {
        if (args.length == 1) {
            packets.sendPrivateMessage(player, MessageType.WARNING, "Usage: /item <itemId> [count]");
            return;
        }
        player.getInventory().addItem(Integer.parseInt(args[1]), 
                args.length > 2 ? Integer.parseInt(args[2]) : 1);
        
        packets.sendFullInventoryUpdate(player);
    }
    
}
