package hu.gerviba.hacknslash.backend.pojo.game;

import java.util.List;

import lombok.Data;

/**
 * Pojo to store a spawned mob
 * @author Gergely Szabó
 */
@Data
public class MobPojo {

    private final long entityId;
    private final MobTemplate template;
    private List<Long> path;
    private double x;
    private double y;
    private volatile double health;
    
    /**
     * Is this mob alive
     * @return true if it is alive
     */
    public boolean isAlive() {
        return health > 0;
    }
    
}
