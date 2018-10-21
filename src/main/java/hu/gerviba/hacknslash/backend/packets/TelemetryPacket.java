package hu.gerviba.hacknslash.backend.packets;

import java.util.List;

import hu.gerviba.hacknslash.backend.model.PlayerEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TelemetryPacket {

    @Data
    @NoArgsConstructor
    public static class PlayerModelStatus {
        private String name;
        private long entityId;
        private double x;
        private double y;
        private int direction;
        private boolean walking;
        private float hp;
        
        public PlayerModelStatus(PlayerEntity user) {
            this.name = user.getName();
            this.entityId = user.getEntityId();
            this.x = user.getX();
            this.y = user.getY();
            this.direction = user.getDirection();
            this.walking = user.isWalking();
            this.hp = (float) (user.getHp() / user.getMaxHp());
        }
    }
        
    private List<PlayerModelStatus> players;
    
    private List<Long> entityRemove;
    
}
