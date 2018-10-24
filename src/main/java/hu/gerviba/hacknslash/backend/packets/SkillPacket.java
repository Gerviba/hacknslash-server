package hu.gerviba.hacknslash.backend.packets;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SkillPacket {

    private int skillUid;
    private int direction;
    private double x;
    private double y;
    
}
