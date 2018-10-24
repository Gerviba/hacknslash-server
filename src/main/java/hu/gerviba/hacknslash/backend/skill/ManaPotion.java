package hu.gerviba.hacknslash.backend.skill;

import org.springframework.beans.factory.annotation.Autowired;

import hu.gerviba.hacknslash.backend.model.PlayerEntity;
import hu.gerviba.hacknslash.backend.services.GlobalPacketService;

public class ManaPotion extends Skill {

    private final double manaIncrease;
    
    @Autowired
    GlobalPacketService packets;
    
    public ManaPotion(int skillUid, int reloadTime, double manaIncrease) {
        super(skillUid, 0, reloadTime);
        this.manaIncrease = manaIncrease;
    }

    @Override
    public void apply(PlayerEntity pe, double x, double y, int direction) {
        pe.setMana(pe.getMana() + manaIncrease);
        packets.sendSelfUpdate(pe);
    }
    
}
