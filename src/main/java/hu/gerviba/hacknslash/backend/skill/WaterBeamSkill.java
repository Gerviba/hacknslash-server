package hu.gerviba.hacknslash.backend.skill;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;

import hu.gerviba.hacknslash.backend.model.PlayerEntity;
import hu.gerviba.hacknslash.backend.services.GlobalPacketService;

public class WaterBeamSkill extends Skill {

    @Autowired
    GlobalPacketService packets;
    
    @Autowired
    ScheduledExecutorService scheduler;
    
    private static int DIRECTION_STAND = 0;
    private static int DIRECTION_LEFT = 1;
    private static int DIRECTION_RIGHT = 2;
    private static int DIRECTION_BACK = 3;
    
    public WaterBeamSkill(int skillUid, double manaCost, int reloadTime) {
        super(skillUid, manaCost, reloadTime);
    }

    @Override
    public void apply(PlayerEntity pe, double x, double y, int direction) {
        pe.setMana(pe.getMana() - getManaCost());
        packets.sendSelfUpdate(pe);
        
        double xMultipiler = direction == DIRECTION_LEFT ? -1
                : direction == DIRECTION_RIGHT ? 1 : 0;
        
        double yMultipiler = direction == DIRECTION_STAND ? 1
                : direction == DIRECTION_BACK ? -1 : 0;
        
        for (int i = 1; i < 7; ++i) {
            double tempX = x + ((0.5 * i) * xMultipiler);
            double tempY = y + ((0.5 * i) * yMultipiler);
            scheduler.schedule(() -> {
               packets.sendMobDamageTo(pe, tempX, tempY, 10); 
            }, -120 + (120 * i) + 1, TimeUnit.MILLISECONDS);
        }
    }

    
}
