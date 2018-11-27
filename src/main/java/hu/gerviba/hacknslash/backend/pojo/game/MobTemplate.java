package hu.gerviba.hacknslash.backend.pojo.game;

import java.util.concurrent.atomic.AtomicLong;

import hu.gerviba.hacknslash.backend.pathfinder.PathFinder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

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
 
    public void increaseRespawnTimer(IngameMap owner) {
        ++respawnTimerCounter;
        if (respawnTimerCounter >= respawnTimer) {
            respawnTimerCounter = 0;
            spawnNew(owner);
        }
    }

    public void recalculatePaths(IngameMap ingameMap) {
        if (spawnedInstance == null || !spawnedInstance.isAlive())
            return;
        spawnedInstance.setPath(ingameMap.getPathFinder().shortestPath(
                PathFinder.coordsToId((int) (spawnedInstance.getX() / SIZE_IN_PIXEL), 
                        (int) (spawnedInstance.getY() / SIZE_IN_PIXEL)), 
                PathFinder.coordsToId((int) (spawnedInstance.getX() / SIZE_IN_PIXEL), 
                        (int) (spawnedInstance.getY() / SIZE_IN_PIXEL))));
    }

    public boolean inDistance(double x, double y, double delta) {
        if (spawnedInstance == null || !spawnedInstance.isAlive())
            return false;
        return Math.sqrt((x - spawnedInstance.getX()) * (x - spawnedInstance.getX()) 
                + (y - spawnedInstance.getY()) * (y - spawnedInstance.getY())) < delta;
    }

    public void damage(int damage) {
        if (spawnedInstance == null || !spawnedInstance.isAlive())
            return;
        System.out.println("I'm just damaged. Uh.");
        spawnedInstance.setHealth(Math.max(0, spawnedInstance.getHealth() - damage));
    }
    
}
