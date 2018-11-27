package hu.gerviba.hacknslash.backend.packets;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Telemetry update packet
 * @author Gergely Szabó
 */
@Data
@NoArgsConstructor
public class TelemetryUpdatePacket {

    private double x;
    private double y;
    private int direction;
    private boolean walking;
    
}
