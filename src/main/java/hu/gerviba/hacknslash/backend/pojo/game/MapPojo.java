package hu.gerviba.hacknslash.backend.pojo.game;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MapPojo {

    private String storeName;
    private String displayName;
    
    // Allowed Regions (xy -> xy)
    // Portals Regions (map, x, y, [server])
    
}
