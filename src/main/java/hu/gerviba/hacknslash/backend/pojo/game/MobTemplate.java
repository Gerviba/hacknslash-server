package hu.gerviba.hacknslash.backend.pojo.game;

import java.util.concurrent.atomic.AtomicLong;

import hu.gerviba.hacknslash.backend.pathfinder.PathFinder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Mob template. 
 * Representation of a mob spawning spot.
 * @author Gergely Szabó
 */
@Slf4j
@Data
public class MobTemplate {
    
    private static AtomicLong MOB_EIDS = new AtomicLong(8000000);
    int SIZE_IN_PIXEL = 32;
    
    private final String name;
    private final double spawnX;
    private final double spawnY;
    private final double damage;
    private final double maxHp;
    private final int level;
    private final String texture;
    private final int exp;
    private final int respawnTimer;
    private int respawnTimerCounter = 0;
    
    private MobPojo spawnedInstance;
    
    /**
     * Spawn new if it is not spawned
     * @param owner IngameMap
     */
    public void spawnNew(IngameMap owner) {
        if (spawnedInstance != null) {
            if (spawnedInstance.isAlive())
                return;
            owner.removeEntity(spawnedInstance.getEntityId());
        }
        log.info("A new mob has been spawned: " + name);
        spawnedInstance = new MobPojo(MOB_EIDS.incrementAndGet(), this);
        spawnedInstance.setHealth(maxHp);
        spawnedInstance.setX(spawnX);
        spawnedInstance.setY(spawnY);
    }
    
    /**
     * Ticks the respawn timer.
     * @param owner IngameMap
     */
    public void increaseRespawnTimer(IngameMap owner) {
        ++respawnTimerCounter;
        if (respawnTimerCounter >= respawnTimer) {
            respawnTimerCounter = 0;
            spawnNew(owner);
        }
    }
    
    /**
     * Recaluclate the shortest path to the nearest player
     * @param ingameMap IngameMap
     */
    public void recalculatePaths(IngameMap ingameMap) {
        if (spawnedInstance == null || !spawnedInstance.isAlive())
            return;
        spawnedInstance.setPath(ingameMap.getPathFinder().shortestPath(
                PathFinder.coordsToId((int) (spawnedInstance.getX() / SIZE_IN_PIXEL), 
                        (int) (spawnedInstance.getY() / SIZE_IN_PIXEL)), 
                PathFinder.coordsToId((int) (spawnedInstance.getX() / SIZE_IN_PIXEL), 
                        (int) (spawnedInstance.getY() / SIZE_IN_PIXEL))));
    }

    /**
     * Is the distance is in delta
     * @param x X coordinate
     * @param y Y coordinate
     * @param delta Distance allowed
     * @return true if it is near enough
     */
    public boolean inDistance(double x, double y, double delta) {
        if (spawnedInstance == null || !spawnedInstance.isAlive())
            return false;
        return Math.sqrt((x - spawnedInstance.getX()) * (x - spawnedInstance.getX()) 
                + (y - spawnedInstance.getY()) * (y - spawnedInstance.getY())) < delta;
    }

    /**
     * Damage the spawned mob if possible
     * @param damage Damage count
     * @return true if the mob died
     */
    public boolean damage(int damage) {
        if (spawnedInstance == null || !spawnedInstance.isAlive())
            return false;
        spawnedInstance.setHealth(Math.max(0, spawnedInstance.getHealth() - damage));
        return spawnedInstance.getHealth() == 0;
    }
    
}
