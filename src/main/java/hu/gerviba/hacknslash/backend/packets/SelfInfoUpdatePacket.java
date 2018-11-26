package hu.gerviba.hacknslash.backend.packets;

import hu.gerviba.hacknslash.backend.model.PlayerEntity;
import lombok.Data;

@Data
public class SelfInfoUpdatePacket {
    
    private long entityId; 
    private int hp;
    private int maxHp;
    private int mana;
    private int maxMana;
    private int exp;
    private int maxExp;
    private int level;
    private int money;
    
    public SelfInfoUpdatePacket(PlayerEntity user) {
        this.entityId = user.getEntityId();
        this.hp = (int) user.getHp();
        this.maxHp = (int) user.getMaxHp();
        this.mana = (int) user.getMana();
        this.maxMana = (int) user.getMaxMana();
        this.exp = user.getExp();
        this.maxExp = user.getMaxExp();
        this.level = user.getLevel();
        this.money = user.getMoney();
    }
    
}
