package hu.gerviba.hacknslash.backend.packets;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * Map load packet
 * @author Gergely Szabó
 */
@Data
@AllArgsConstructor
public class MapLoadPacket {
    
    /**
     * Layer type
     * @author Gergely Szabó
     */
    @AllArgsConstructor
    public static enum LayerType {
        BACKGROUND("BG_FRAGMENT", "B"),
        FOREGROUND("FG_FRAGMENT", "F"),
        MIDDLE("MID_FRAGMENT", "M"),
        ;
        @Getter
        private String command;
        @Getter
        private String indicator;
    }
    
    /**
     * Map layer info
     * @author Gergely Szabó
     */
    @Data
    @AllArgsConstructor
    public static class MapLayerInfo {
        
        /**
         * Background part
         * @author Gergely Szabó
         */
        @Data
        @AllArgsConstructor
        public static class BackgroundPart {
            int x;
            int y;
            char character;
            String name;
            List<int[]> places;
        }
        
        List<BackgroundPart> parts;
        
    }

    /**
     * Static object info
     * @author Gergely Szabó
     */
    @Data
    @AllArgsConstructor
    public static class StaticObjectInfo {

        private String texture;
        private double x;
        private double y;
        private double anchor;
        
    }
    
    private String name;
    private String displayName;
    private String texture;
    private double spawnX;
    private double spawnY;
    private MapLayerInfo foreground;
    private MapLayerInfo background;
    private MapLayerInfo middle;
    private String backgroundColor;
    private List<StaticObjectInfo> objects;
    
}
