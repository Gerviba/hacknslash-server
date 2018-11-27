package hu.gerviba.hacknslash.backend.pojo.game;

import com.fasterxml.jackson.annotation.JsonIgnore;

import hu.gerviba.hacknslash.backend.packets.MapLoadPacket.StaticObjectInfo;
import lombok.Data;

/**
 * Pojo to store static object (such as: chest)
 * @author Gergely Szabó
 */
@Data
public class StaticObjectPojo {

    private final String texture;
    private final double x;
    private final double y;
    private final double anchor;
    
    @JsonIgnore
    private final String action;
    
    /**
     * Type converter
     * @return StaticObjectPojo -> StaticObjectInfo
     */
    public StaticObjectInfo toPacketType() {
        return new StaticObjectInfo(texture, x, y, anchor);
    }
}
