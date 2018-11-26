package hu.gerviba.hacknslash.backend.pojo.game;

import lombok.Data;

@Data
public class MobTemplatePojo {
    
    private final String name;
    private final double spawnX;
    private final double spawnY;
    private final double maxHp;
    private final int level;
    private final String texture;
    private final int exp;
    private final int respawnTimer;
    private int respawnTimerCounter;
    
}
