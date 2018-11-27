package hu.gerviba.hacknslash.backend.skill;

import org.springframework.beans.factory.annotation.Autowired;

import hu.gerviba.hacknslash.backend.model.PlayerEntity;
import hu.gerviba.hacknslash.backend.services.GlobalPacketService;

/**
 * Increases mana count
 * @author Gergely Szabó
 */
public class ManaPotion extends Skill {

    private final double manaIncrease;
    
    @Autowired
    GlobalPacketService packets;
    
    /**
     * Skill constructor
     * @param skillUid Unique ID of the skill
     * @param manaCost Mana cost of casing the skill
     * @param reloadTime Time to reload
     */
    public ManaPotion(int skillUid, int reloadTime, double manaIncrease) {
        super(skillUid, 0, reloadTime);
        this.manaIncrease = manaIncrease;
    }
    
    /**
     * Cast the skill, register future damage coordinates
     */
    @Override
    public void apply(PlayerEntity pe, double x, double y, int direction) {
        pe.setMana(pe.getMana() + manaIncrease);
        packets.sendSelfUpdate(pe);
    }
    
}
