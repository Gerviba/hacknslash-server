package hu.gerviba.hacknslash.backend.skill;

import hu.gerviba.hacknslash.backend.model.PlayerEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class Skill {

    @Getter
    private final int skillUid;
    @Getter
    private final double manaCost;
    @Getter
    private final int reloadTime;
    
    /**
     * Cast the skill, register future damage coordinates
     * @param pe Player
     * @param x X coordinate
     * @param yY coordinate
     * @param direction Direction
     */
    public abstract void apply(PlayerEntity pe, double x, double y, int direction);
    
}
