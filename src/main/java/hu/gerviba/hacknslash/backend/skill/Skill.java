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
    
    public abstract void apply(PlayerEntity pe, double x, double y, int direction);
    
}
