package hu.gerviba.hacknslash.backend.packets;

import java.util.List;

import lombok.Data;

@Data
public class ItemChangePacket {
    
    @Data
    public static class ChangePart {
        private final int slot;
        private final int itemId;
        private final int count;
    }
    
    public enum ChangeMethod {
        OVERRIDE,
        ADD;
    }
    
    private final ChangeMethod method;
    private final List<ChangePart> changes;
    
}
