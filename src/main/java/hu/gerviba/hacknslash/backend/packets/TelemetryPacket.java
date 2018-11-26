package hu.gerviba.hacknslash.backend.packets;

import java.util.List;

import hu.gerviba.hacknslash.backend.model.PlayerEntity;
import lombok.Data;

@Data
public class TelemetryPacket {

    @Data
    public static class PlayerModelStatus {
        private String name;
        private long entityId;
        private double x;
        private double y;
        private int direction;
        private boolean walking;
        private float hp;
        
        private String base;
        private String weapon;
        private String helmet;
        private String armor;
        private String boots;
        
        public PlayerModelStatus(PlayerEntity user) {
            this.name = user.getName();
            this.entityId = user.getEntityId();
            this.x = user.getX();
            this.y = user.getY();
            this.direction = user.getDirection();
            this.walking = user.isWalking();
            this.hp = (float) (user.getHp() / user.getMaxHp());
            
            this.base = user.getBase();
            this.weapon = user.getWeapon();
            this.helmet = user.getHelmet();
            this.armor = user.getArmor();
            this.boots = user.getBoots();
        }
    }
    
    @Data
    public class MobStatus {
        private long entityId;
        private String name;
        private double targetX;
        private double targetY;
        private int hp;
    }
        
    private List<PlayerModelStatus> players;
    private List<Long> entityRemove;
    private List<MobStatus> mobs;
    
}
