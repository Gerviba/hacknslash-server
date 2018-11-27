package hu.gerviba.hacknslash.backend.pojo.game;

import java.util.List;

import lombok.Data;

@Data
public class MobPojo {

    private final long entityId;
    private final MobTemplate template;
    private List<Long> path;
    private double x;
    private double y;
    private volatile double health;
    
    public boolean isAlive() {
        return health > 0;
    }
    
}
