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
import hu.gerviba.hacknslash.backend.packets.TelemetryPacket.MobStatus;
import hu.gerviba.hacknslash.backend.packets.TelemetryPacket.PlayerModelStatus;
import hu.gerviba.hacknslash.backend.pathfinder.PathFinder;
import lombok.Data;
import lombok.Getter;

@Data
public class IngameMap {

    private final String storeName;
    private final String displayName;
    private final String texture;
    
    private MapLoadPacket mapLoadPacket;
    private final double spawnX;
    private final double spawnY;
    private final String backgroundColor;
    
    private final Map<String, PlayerEntity> players = new ConcurrentHashMap<>();
    private final List<MobTemplate> mobs = Collections.synchronizedList(new LinkedList<>());
    private final List<StaticObjectPojo> objects = Collections.synchronizedList(new LinkedList<>());
    
    @Getter
    private PathFinder pathFinder;
    private LinkedList<Long> removedEntities = new LinkedList<>();
    
    /**
     * Initialize path finder
     */
    public void init() {
        pathFinder = new PathFinder(mapLoadPacket.getBackground().getParts()
                .stream()
                .map(part -> (Long) PathFinder.coordsToId(part.getX(), part.getY()))
                .collect(Collectors.toList()));
    }
    
    /**
     * Add mobs
     * @param mobs Mob spawning spots
     */
    public void addMobs(List<MobTemplate> mobs) {
        this.mobs.addAll(mobs);
    }

    /**
     * Add static objects
     * @param objects Static object like chests
     */
    public void addObjects(List<StaticObjectPojo> objects) {
        this.objects.addAll(objects);
    }
    
    /**
     * Generate telemetry packet
     * @return A telemetry packet with the current data
     */
    public TelemetryPacket generatePacketFormMap() {
        TelemetryPacket packet = new TelemetryPacket();
        packet.setPlayers(players.values().stream()
                .map(user -> new PlayerModelStatus(user))
                .collect(Collectors.toList()));
        packet.setMobs(mobs.stream()
                .filter(mob -> mob.getSpawnedInstance() != null)
                .map(mob -> new MobStatus(
                        mob.getSpawnedInstance().getEntityId(),
                        mob.getName(),
                        mob.getTexture(),
                        (mob.getSpawnedInstance().getX() + 0.5) * 64,
                        (mob.getSpawnedInstance().getY() + 0.5) * 64,
                        (float) (mob.getSpawnedInstance().getHealth() / mob.getMaxHp())))
                .collect(Collectors.toList()));
        synchronized (removedEntities) {
            packet.setEntityRemove(removedEntities.stream()
                    .collect(Collectors.toList()));
            removedEntities.clear();
        }
        return packet;
    }

    /**
     * Update positions and informations
     * @param messaging
     */
    public void updateTelemetry(SimpMessagingTemplate messaging) {
        TelemetryPacket packet = generatePacketFormMap();
        players.values().forEach(user -> messaging
                .convertAndSendToUser(user.getSessionId(), "/topic/telemetry", packet));
    }
    
    /**
     * Remove entity from map
     * @param entityId The entity ID of the entity
     */
    public void removeEntity(long entityId) {
        synchronized (removedEntities) {
            removedEntities.add(entityId);
        }
    }
    
    /**
     * Palyer disappeared in the map
     * @param pe player
     */
    public void removePlayer(PlayerEntity pe) {
        players.remove(pe.getSessionId());
        synchronized (removedEntities) {
            removedEntities.add(pe.getEntityId());
        }
    }
    
    /**
     * Player appeared in the map
     * @param pe player
     */
    public void addPlayer(PlayerEntity pe) {
        players.put(pe.getSessionId(), pe);
    }
    
    /**
     * Recalculate the path
     */
    public void recalculatePaths() {
//        mobs.forEach(m -> m.recalculatePaths(this));
    }
    
    /**
     * Respawn timer ticks
     */
    public void spawnTimerTicks() {
        mobs.forEach(m -> m.increaseRespawnTimer(this));
    }
    
    // TODO: Portals Regions (map, x, y, [server])
    
}
