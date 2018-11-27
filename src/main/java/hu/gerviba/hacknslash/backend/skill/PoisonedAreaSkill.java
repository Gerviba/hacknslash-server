package hu.gerviba.hacknslash.backend.skill;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;

import hu.gerviba.hacknslash.backend.model.PlayerEntity;
import hu.gerviba.hacknslash.backend.services.GlobalPacketService;

public class PoisonedAreaSkill extends Skill {

    @Autowired
    GlobalPacketService packets;
    
    @Autowired
    ScheduledExecutorService scheduler;
    
    private final double[] X_COORDS, Y_COORDS;

    public PoisonedAreaSkill(int skillUid, double manaCost, int reloadTime) {
        super(skillUid, manaCost, reloadTime);
        
        X_COORDS = new double[] {
                0.4,
                0.1,
                0.0,
                0.4,
                -0.4,
                -0.5,
                0.1,
                0.1,
                -0.1,
                -0.3
        };
        Y_COORDS = new double[] {
                0.0,
                0.1,
                -0.2,
                -0.4,
                0.3,
                -0.1,
                -0.4,
                0.3,
                0.2,
                -0.4
        };
        
    }
    
    @Override
    public void apply(PlayerEntity pe, double x, double y, int direction) {
        pe.setMana(pe.getMana() - getManaCost());
        packets.sendSelfUpdate(pe);
        
        for (int i = 0; i < X_COORDS.length; ++i) {
            double tempX = x + X_COORDS[i];
            double tempY = y + Y_COORDS[i];
            scheduler.schedule(() -> {
                packets.sendMobDamageTo(pe, tempX, tempY, 5); 
            }, 1000, TimeUnit.MILLISECONDS);
            scheduler.schedule(() -> {
                packets.sendMobDamageTo(pe, tempX, tempY, 4); 
            }, 2000, TimeUnit.MILLISECONDS);
            scheduler.schedule(() -> {
                packets.sendMobDamageTo(pe, tempX, tempY, 3); 
            }, 3000, TimeUnit.MILLISECONDS);
        }
    }

}
