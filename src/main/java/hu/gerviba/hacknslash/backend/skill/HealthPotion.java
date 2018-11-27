package hu.gerviba.hacknslash.backend.skill;

import org.springframework.beans.factory.annotation.Autowired;

import hu.gerviba.hacknslash.backend.model.PlayerEntity;
import hu.gerviba.hacknslash.backend.services.GlobalPacketService;

/**
 * Increases health points
 * @author Gergely Szabó
 */
public class HealthPotion extends Skill {

    private final double hpIncrease;
    
    @Autowired
    GlobalPacketService packets;
    
    /**
     * Skill constructor
     * @param skillUid Unique ID of the skill
     * @param manaCost Mana cost of casing the skill
     * @param reloadTime Time to reload
     */
    public HealthPotion(int skillUid, int reloadTime, double hpIncrease) {
        super(skillUid, 0, reloadTime);
        this.hpIncrease = hpIncrease;
    }
    
    /**
     * Cast the skill, register future damage coordinates
     */
    @Override
    public void apply(PlayerEntity pe, double x, double y, int direction) {
        pe.setHp(pe.getHp() + hpIncrease);
        packets.sendSelfUpdate(pe);
    }

}
