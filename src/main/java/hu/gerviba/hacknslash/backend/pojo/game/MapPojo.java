package hu.gerviba.hacknslash.backend.pojo.game;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import hu.gerviba.hacknslash.backend.model.PlayerEntity;
import hu.gerviba.hacknslash.backend.packets.MapLoadPacket;
import hu.gerviba.hacknslash.backend.packets.TelemetryPacket;
import hu.gerviba.hacknslash.backend.packets.TelemetryPacket.PlayerModelStatus;
import hu.gerviba.hacknslash.backend.pathfinder.PathFinder;
import lombok.Data;

@Data
public class MapPojo {

    private final String storeName;
    private final String displayName;
    private final String texture;
    
    private MapLoadPacket mapLoadPacket;
    private final double spawnX;
    private final double spawnY;
    private final String backgroundColor;
    
    private final Map<String, PlayerEntity> players = new ConcurrentHashMap<>();
    private final List<MobTemplatePojo> mobs = Collections.synchronizedList(new LinkedList<>());
    private final List<StaticObjectPojo> objects = Collections.synchronizedList(new LinkedList<>());
    
    private PathFinder pathFinder;
    private LinkedList<Long> removedEntities = new LinkedList<>();
    
    public void init() {
        pathFinder = new PathFinder(mapLoadPacket.getBackground().getParts()
                .stream()
                .map(part -> (Long) PathFinder.coordsToId(part.getX(), part.getY()))
                .collect(Collectors.toList()));
    }
    
    public void addMobs(List<MobTemplatePojo> mobs) {
        this.mobs.addAll(mobs);
    }

    public void addObjects(List<StaticObjectPojo> objects) {
        this.objects.addAll(objects);
    }
    
    public TelemetryPacket generatePacketFormMap() {
        TelemetryPacket packet = new TelemetryPacket();
        packet.setPlayers(players.values().stream()
                .map(user -> new PlayerModelStatus(user))
                .collect(Collectors.toList()));
        synchronized (removedEntities) {
            packet.setEntityRemove(removedEntities.stream()
                    .collect(Collectors.toList()));
            removedEntities.clear();
        }
        return packet;
    }

    public void updateTelemetry(SimpMessagingTemplate messaging) {
        TelemetryPacket packet = generatePacketFormMap();
        players.values().forEach(user -> messaging
                .convertAndSendToUser(user.getSessionId(), "/topic/telemetry", packet));
    }
    
    public void removeEntity(long entityId) {
        synchronized (removedEntities) {
            removedEntities.add(entityId);
        }
    }
    
    public void removePlayer(PlayerEntity pe) {
        players.remove(pe.getSessionId());
        synchronized (removedEntities) {
            removedEntities.add(pe.getEntityId());
        }
    }
    
    public void addPlayer(PlayerEntity pe) {
        players.put(pe.getSessionId(), pe);
    }
    
    // TODO: Allowed Regions (xy -> xy)
    // TODO: Portals Regions (map, x, y, [server])
    
}
