package hu.gerviba.hacknslash.backend.skill;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;

import hu.gerviba.hacknslash.backend.model.PlayerEntity;
import hu.gerviba.hacknslash.backend.services.GlobalPacketService;

/**
 * Circle of flame made of 12 flame particles
 * @author Gergely Szabó
 */
public class FlameCircleSkill extends Skill {
    
    @Autowired
    GlobalPacketService packets;
    
    @Autowired
    ScheduledExecutorService scheduler;
    
    private final double[] X_COORDS, Y_COORDS;
    private final int FLAMES = 12;
    
    /**
     * Skill constructor
     * @param skillUid Unique ID of the skill
     * @param manaCost Mana cost of casing the skill
     * @param reloadTime Time to reload
     */
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

    /**
     * Cast the skill, register future damage coordinates
     */
    @Override
    public void apply(PlayerEntity pe, double x, double y, int direction) {
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
            }, 1220, TimeUnit.MILLISECONDS);
            scheduler.schedule(() -> {
                packets.sendMobDamageTo(pe, tempX, tempY, 8);
            }, 1920, TimeUnit.MILLISECONDS);
        }
    }

}
