package hu.gerviba.hacknslash.backend.skill;

import org.springframework.beans.factory.annotation.Autowired;

import hu.gerviba.hacknslash.backend.model.PlayerEntity;
import hu.gerviba.hacknslash.backend.services.GlobalPacketService;

public class HealthPotion extends Skill {

    private final double hpIncrease;
    
    @Autowired
    GlobalPacketService packets;
    
    public HealthPotion(int skillUid, int reloadTime, double hpIncrease) {
        super(skillUid, 0, reloadTime);
        this.hpIncrease = hpIncrease;
    }

    @Override
    public void apply(PlayerEntity pe, double x, double y, int direction) {
        pe.setHp(pe.getHp() + hpIncrease);
        packets.sendSelfUpdate(pe);
    }

}
