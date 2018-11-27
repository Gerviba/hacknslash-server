package hu.gerviba.hacknslash.backend.skill;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;

import hu.gerviba.hacknslash.backend.model.PlayerEntity;
import hu.gerviba.hacknslash.backend.services.GlobalPacketService;

public class ThunderSkill extends Skill {

    @Autowired
    GlobalPacketService packets;
    
    @Autowired
    ScheduledExecutorService scheduler;
    
    private static int DIRECTION_STAND = 0;
    private static int DIRECTION_LEFT = 1;
    private static int DIRECTION_RIGHT = 2;
    private static int DIRECTION_BACK = 3;
    
    private final double[] X_COORDS, Y_COORDS;

    public ThunderSkill(int skillUid, double manaCost, int reloadTime) {
        super(skillUid, manaCost, reloadTime);
        
        X_COORDS = new double[] {
                0.4,
                0.1,
                0.0,
                0.4,
                -0.4,
                -0.5,
                0.1,
                0.1,
                -0.1,
                -0.3,
                -0.4
        };
        Y_COORDS = new double[] {
                0.0,
                0.1,
                -0.2,
                -0.4,
                0.3,
                -0.1,
                -0.4,
                0.3,
                0.2,
                -0.4,
                -0.3
        };
        
    }
    
    @Override
    public void apply(PlayerEntity pe, double x, double y, int direction) {
        pe.setMana(pe.getMana() - getManaCost());
        packets.sendSelfUpdate(pe);
        
        double xShift = direction == DIRECTION_LEFT ? -1.5
                : direction == DIRECTION_RIGHT ? 1.5 : 0;
        
        double yShift = direction == DIRECTION_STAND ? 1.5
                : direction == DIRECTION_BACK ? -1.5 : 0;
        
        for (int i = 0; i < X_COORDS.length; ++i) {
            double tempX = x + X_COORDS[i] + xShift;
            double tempY = y + Y_COORDS[i] + yShift;
            scheduler.schedule(() -> {
                packets.sendMobDamageTo(pe, tempX, tempY, 3);
            }, 520, TimeUnit.MILLISECONDS);
            scheduler.schedule(() -> {
                packets.sendMobDamageTo(pe, tempX, tempY, 2);
            }, 1240, TimeUnit.MILLISECONDS);
            scheduler.schedule(() -> {
                packets.sendMobDamageTo(pe, tempX, tempY, 1);
            }, 2400, TimeUnit.MILLISECONDS);
        }
    }

}
