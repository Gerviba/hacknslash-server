package hu.gerviba.hacknslash.backend.skill;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;

import hu.gerviba.hacknslash.backend.model.PlayerEntity;
import hu.gerviba.hacknslash.backend.services.GlobalPacketService;

/**
 * A purple beam of magic
 * @author Gergely Szabó
 */
public class PurpleMagicSkill extends Skill {

    @Autowired
    GlobalPacketService packets;
    
    @Autowired
    ScheduledExecutorService scheduler;

    private static int DIRECTION_STAND = 0;
    private static int DIRECTION_LEFT = 1;
    private static int DIRECTION_RIGHT = 2;
    private static int DIRECTION_BACK = 3;
    
    /**
     * Skill constructor
     * @param skillUid Unique ID of the skill
     * @param manaCost Mana cost of casing the skill
     * @param reloadTime Time to reload
     */
    public PurpleMagicSkill(int skillUid, double manaCost, int reloadTime) {
        super(skillUid, manaCost, reloadTime);
    }

    /**
     * Cast the skill, register future damage coordinates
     */
    @Override
    public void apply(PlayerEntity pe, double x, double y, int direction) {
        pe.setMana(pe.getMana() - getManaCost());
        packets.sendSelfUpdate(pe);
        
        double xMultipiler = direction == DIRECTION_LEFT ? -1
                : direction == DIRECTION_RIGHT ? 1 : 0;
        
        double yMultipiler = direction == DIRECTION_STAND ? 1
                : direction == DIRECTION_BACK ? -1 : 0;
        
        for (int i = 1; i < 14; ++i) {
            double tempX = x + ((0.25 * i) * xMultipiler);
            double tempY = y + ((0.25 * i) * yMultipiler);
            scheduler.schedule(() -> {
                packets.sendMobDamageTo(pe, tempX, tempY, 12); 
            }, -80 + (80 * i) + 1, TimeUnit.MILLISECONDS);
        }
    }

    
    
}
