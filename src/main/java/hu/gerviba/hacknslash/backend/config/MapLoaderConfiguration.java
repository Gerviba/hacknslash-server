package hu.gerviba.hacknslash.backend.config;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import hu.gerviba.hacknslash.backend.ConfigProfile;
import hu.gerviba.hacknslash.backend.packets.MapLoadPacket;
import hu.gerviba.hacknslash.backend.packets.MapLoadPacket.LayerType;
import hu.gerviba.hacknslash.backend.packets.MapLoadPacket.MapLayerInfo;
import hu.gerviba.hacknslash.backend.packets.MapLoadPacket.MapLayerInfo.BackgroundPart;
import hu.gerviba.hacknslash.backend.packets.MapLoadPacket.StaticObjectInfo;
import hu.gerviba.hacknslash.backend.pojo.game.IngameMap;
import hu.gerviba.hacknslash.backend.pojo.game.MobTemplate;
import hu.gerviba.hacknslash.backend.pojo.game.StaticObjectPojo;
import hu.gerviba.hacknslash.backend.services.CustomLoggingService;
import lombok.extern.slf4j.Slf4j;

@Profile(ConfigProfile.GAME_SERVER)
@Configuration
@Slf4j
public class MapLoaderConfiguration {

    @Value("${game.resources-dir:resources}")
    String resourcesDir;

    @Autowired
    CustomLoggingService logger;
    
    @Bean
    ConcurrentHashMap<String, IngameMap> mapConfig() {
        ConcurrentHashMap<String, IngameMap> maps = new ConcurrentHashMap<>();
        
        for (File map : new File(resourcesDir, "maps").listFiles()) {
            try {
                File mapFile = new File(map.getAbsolutePath(), map.getName() + ".map");
                if (mapFile.exists())
                    maps.put(map.getName(), loadMap(map.getName(), mapFile));
            } catch (Exception e) {
                log.error("Failed to load map: " + map.getName(), e);
            }
        }
        
        maps.values().forEach(IngameMap::init);
        return maps;
    }

    private IngameMap loadMap(String name, File mapFile) throws IOException {
        log.info("Loading map: " + name + " (" + mapFile.getAbsolutePath() + ")");
        logger.info("Loading map: " + name);
        List<String> lines = Files.readAllLines(mapFile.toPath(), StandardCharsets.UTF_8);
        IngameMap map = loadMeta(lines, name);
        
        map.addMobs(loadMobs(lines));
        List<StaticObjectPojo> objects = loadStaticObjects(lines);
        map.addObjects(objects);
        
        lines.stream()
            .map(line -> line.split("[ \t]+"))
            .forEach(params -> {
                try {
                    if (params[0].equals("BACKGROUND")) {
                        map.setMapLoadPacket(loadMapLoadPacket(
                                map.getStoreName(), map.getDisplayName(), 
                                map.getTexture(), mapFile.getParent() + "/" + params[1],
                                map.getSpawnX(), map.getSpawnY(), map.getBackgroundColor(),
                                objects));
                    }
                } catch (Exception e) {
                    log.error("Failed to load map: " + map.getStoreName());
                }
            });
        
        return map;
    }

    private IngameMap loadMeta(List<String> lines, String name) throws IOException {
        String displayName = name;
        String texture = "na";
        double spawnX = 0;
        double spawnY = 0;
        String backgroundColor = "000000";
        
        List<String[]> meta = lines.stream()
                .filter(line -> line.startsWith("META "))
                .map(line -> line.substring(5).split("[ \t]+", 2))
                .filter(params -> params.length == 2)
                .collect(Collectors.toList());
        
        for (String[] params : meta) {
            if (params[0].equalsIgnoreCase("DISPLAY_NAME")) {
                displayName = params[1];
            } else if (params[0].equalsIgnoreCase("TEXTURE")) {
                texture = params[1];
            } else if (params[0].equalsIgnoreCase("SPAWN")) {
                spawnX = Double.parseDouble(params[1].split("[ \\t]+")[0]);
                spawnY = Double.parseDouble(params[1].split("[ \\t]+")[1]);
            } else if (params[0].equalsIgnoreCase("BACKGROUND_COLOR")) {
                backgroundColor = params[1];
            }
        }
        
        return new IngameMap(name, displayName, texture, spawnX, spawnY, backgroundColor);
    }
    
    private MapLoadPacket loadMapLoadPacket(String name, String displayName, 
            String texture, String fileName, double x, double y, String color,
            List<StaticObjectPojo> objects) throws IOException {
        
        List<String> lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
        return new MapLoadPacket(name, displayName, texture, x, y, 
                loadMapLayer(LayerType.FOREGROUND, lines),
                loadMapLayer(LayerType.BACKGROUND, lines),
                loadMapLayer(LayerType.MIDDLE, lines), 
                color, 
                convertToPacketType(objects));
    }

    private List<StaticObjectInfo> convertToPacketType(List<StaticObjectPojo> objects) {
        return objects.stream()
                .map(StaticObjectPojo::toPacketType)
                .collect(Collectors.toList());
    }

    private MapLayerInfo loadMapLayer(LayerType layer, List<String> lines) throws IOException {
        List<BackgroundPart> parts = lines.stream()
                .filter(line -> line.startsWith("DEF "))
                .map(line -> line.substring(4).split("[ \t]+", 4))
                .filter(params -> params.length == 4)
                .map(params -> new BackgroundPart(
                        Integer.parseInt(params[2]),
                        Integer.parseInt(params[3]),
                        params[0].charAt(0),
                        params[1],
                        new LinkedList<int[]>()))
                .collect(Collectors.toList());
        
        int fragmentX = 0;
        int fragmentStartX = 0;
        int fragmentY = 0;
        
        for (int i = 0; i < lines.size(); ++i) {
            String line = lines.get(i);
            
            if (line.startsWith(layer.getCommand())) {
                String params[] = line.split("[ \\t]+", 4);
                fragmentX = Integer.parseInt(params[2]);
                fragmentStartX = fragmentX;
                fragmentY = Integer.parseInt(params[3]);
                
            } else if (line.startsWith(layer.getIndicator() + " ")) {
                for (char c : line.substring(2).toCharArray()) {
                    BackgroundPart part = getPartByChar(c, parts);
                    if (part != null)
                        part.getPlaces().add(new int[] {fragmentX, fragmentY});
                    ++fragmentX;
                }
                ++fragmentY;
                fragmentX = fragmentStartX;
            }
        }
        
        return new MapLayerInfo(parts.stream()
                .filter(part -> part.getPlaces().size() > 0)
                .filter(part -> !part.getName().equals("NOT_SET"))
                .collect(Collectors.toList()));
    }
    
    private List<StaticObjectPojo> loadStaticObjects(List<String> lines) {
        return lines.stream()
                .filter(line -> line.startsWith("OBJ "))
                .map(line -> line.substring(4).split("[ \t]+", 5))
                .filter(params -> params.length == 5)
                .map(params -> new StaticObjectPojo(params[0], 
                        Double.parseDouble(params[1]),
                        Double.parseDouble(params[2]),
                        Double.parseDouble(params[3]),
                        params[4]))
                .peek(sop -> log.info("- Loaded object: " + sop.getTexture() 
                        + " " + sop.getX() + " " + sop.getY()))
                .collect(Collectors.toList());
    }
    
    private List<MobTemplate> loadMobs(List<String> lines) {
        return lines.stream()
                .filter(line -> line.startsWith("MOB "))
                .map(line -> line.substring(4).split("[ \t]+", 9))
                .filter(params -> params.length == 9)
                .map(params -> new MobTemplate(
                        params[0].replace('_', ' '), 
                        Double.parseDouble(params[1]), 
                        Double.parseDouble(params[2]), 
                        Double.parseDouble(params[3]), 
                        Double.parseDouble(params[4]),
                        Integer.parseInt(params[5]),
                        params[6],
                        Integer.parseInt(params[7]),
                        Integer.parseInt(params[8])))
                .collect(Collectors.toList());
    }

    private BackgroundPart getPartByChar(char c, List<BackgroundPart> parts) {
        return parts.stream().filter(x -> x.getCharacter() == c).findFirst().orElse(null);
    }
    
}
