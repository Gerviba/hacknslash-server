package hu.gerviba.hacknslash.backend.pojo.game;

import com.fasterxml.jackson.annotation.JsonIgnore;

import hu.gerviba.hacknslash.backend.packets.MapLoadPacket.StaticObjectInfo;
import lombok.Data;

@Data
public class StaticObjectPojo {

    private final String texture;
    private final double x;
    private final double y;
    private final double anchor;
    
    @JsonIgnore
    private final String action;
    
    public StaticObjectInfo toPacketType() {
        return new StaticObjectInfo(texture, x, y, anchor);
    }
}
