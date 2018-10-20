package hu.gerviba.hacknslash.backend.pojo.game;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import hu.gerviba.hacknslash.backend.model.PlayerEntity;
import hu.gerviba.hacknslash.backend.packets.MapLoadPacket;
import hu.gerviba.hacknslash.backend.packets.TelemetryPacket;
import lombok.Data;

@Data
public class MapPojo {

    private final String storeName;
    private final String displayName;
    private final String texture;
    
    private MapLoadPacket mapLoadPacket;
    private final int spawnX;
    private final int spawnY;
    private final String backgroundColor;
    
    private final Map<String, PlayerEntity> players = new ConcurrentHashMap<>();
    
    public TelemetryPacket generatePacketFormMap() {
        return null;
    }

    
    // Allowed Regions (xy -> xy)
    // Portals Regions (map, x, y, [server])
    
}
