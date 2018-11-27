package hu.gerviba.hacknslash.backend.packets;

import lombok.Data;

/**
 * Item changed packet
 * @author Gergely Szabó
 */
@Data
public class ItemChangeUpdatePacket {

    private final int slotFrom;
    private final int slotTo;
    
}
