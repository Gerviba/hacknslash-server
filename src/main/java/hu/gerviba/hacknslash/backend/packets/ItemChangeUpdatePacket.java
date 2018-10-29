package hu.gerviba.hacknslash.backend.packets;

import lombok.Data;

@Data
public class ItemChangeUpdatePacket {

    private final int slotFrom;
    private final int slotTo;
    
}
