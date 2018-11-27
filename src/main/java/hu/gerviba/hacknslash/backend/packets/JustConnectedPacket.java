package hu.gerviba.hacknslash.backend.packets;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Just connected packet
 * @author Gergely Szabó
 *
 */
@Data
@AllArgsConstructor
public class JustConnectedPacket {

    private int height;
    private int width;
    private int scale;
    
}
