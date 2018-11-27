package hu.gerviba.hacknslash.backend.skill;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;

import hu.gerviba.hacknslash.backend.model.PlayerEntity;
import hu.gerviba.hacknslash.backend.services.GlobalPacketService;

public class FlameCircleSkill extends Skill {
    
    @Autowired
    GlobalPacketService packets;
    
    @Autowired
    ScheduledExecutorService scheduler;
    
    private final double[] X_COORDS, Y_COORDS;
    private final int FLAMES = 12;
    
    public FlameCircleSkill(int skillUid, double manaCost, int reloadTime) {
        super(skillUid, manaCost, reloadTime);
        
        final double r = 0.75;
        X_COORDS = new double[FLAMES];
        Y_COORDS = new double[FLAMES];
        
        for (int k = 0; k < FLAMES; ++k) {
            X_COORDS[k] = r * Math.cos((k * (Math.PI * 2)) / FLAMES);
            Y_COORDS[k] = r * Math.sin((k * (Math.PI * 2)) / FLAMES);
        }
    }

    @Override
    public void apply(PlayerEntity pe, double x, double y, int direction) {
        System.out.println("----- 2");
        pe.setMana(pe.getMana() - getManaCost());
        packets.sendSelfUpdate(pe);
        
        for (int i = 0; i < FLAMES; ++i) {
            double tempX = x + X_COORDS[i];
            double tempY = y + Y_COORDS[i];
            scheduler.schedule(() -> {
                packets.sendMobDamageTo(pe, tempX, tempY, 8);
            }, 520, TimeUnit.MILLISECONDS);
            scheduler.schedule(() -> {
                packets.sendMobDamageTo(pe, tempX, tempY, 8);
            }, 1520, TimeUnit.MILLISECONDS);
            scheduler.schedule(() -> {
                packets.sendMobDamageTo(pe, tempX, tempY, 8);
            }, 2520, TimeUnit.MILLISECONDS);
        }
    }

}
