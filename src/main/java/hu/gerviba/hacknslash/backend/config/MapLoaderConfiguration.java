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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import hu.gerviba.hacknslash.backend.ConfigProfile;
import hu.gerviba.hacknslash.backend.packets.MapLoadPacket;
import hu.gerviba.hacknslash.backend.packets.MapLoadPacket.LayerType;
import hu.gerviba.hacknslash.backend.packets.MapLoadPacket.MapLayerInfo;
import hu.gerviba.hacknslash.backend.packets.MapLoadPacket.MapLayerInfo.BackgroundPart;
import hu.gerviba.hacknslash.backend.pojo.game.MapPojo;
import lombok.extern.slf4j.Slf4j;

@Profile(ConfigProfile.GAME_SERVER)
@Configuration
@Slf4j
public class MapLoaderConfiguration {

    @Value("${game.resources-dir:resources}")
    String resourcesDir;
    
    @Bean
    ConcurrentHashMap<String, MapPojo> mapConfig() {
        ConcurrentHashMap<String, MapPojo> maps = new ConcurrentHashMap<>();
        
        for (File map : new File(resourcesDir, "maps").listFiles()) {
            try {
                File mapFile = new File(map.getAbsolutePath(), map.getName() + ".map");
                if (mapFile.exists())
                    maps.put(map.getName(), loadMap(map.getName(), mapFile));
            } catch (Exception e) {
                log.error("Failed to load map: " + map.getName(), e);
            }
        }
        
        return maps;
    }

    private MapPojo loadMap(String name, File mapFile) throws IOException {
        log.info("Loading map: " + name + " (" + mapFile.getAbsolutePath() + ")");
        List<String> lines = Files.readAllLines(mapFile.toPath(), StandardCharsets.UTF_8);
        MapPojo map = loadMeta(lines, name);
        
        lines.stream()
            .map(line -> line.split("[ \t]+"))
            .forEach(params -> {
                try {
                    if (params[0].equals("BACKGROUND")) {
                        map.setMapLoadPacket(loadMapLoadPacket(
                                map.getStoreName(), map.getDisplayName(), 
                                map.getTexture(), mapFile.getParent() + "/" + params[1],
                                map.getSpawnX(), map.getSpawnY()));
                    }
                } catch (Exception e) {
                    log.error("Failed to load map: " + map.getStoreName());
                }
            });
        
        return map;
    }

    private MapPojo loadMeta(List<String> lines, String name) throws IOException {
        String displayName = name;
        String texture = "na";
        int spawnX = 0;
        int spawnY = 0;
        
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
                spawnX = Integer.parseInt(params[1].split("[ \\t]+")[0]);
                spawnY = Integer.parseInt(params[1].split("[ \\t]+")[1]);
            }
        }
        
        return new MapPojo(name, displayName, texture, spawnX, spawnY);
    }
    
    private MapLoadPacket loadMapLoadPacket(String name, String displayName, 
            String texture, String fileName, int x, int y) throws IOException {
        return new MapLoadPacket(name, displayName, texture, x, y, 
                loadMapLayer(LayerType.FOREGROUND, fileName),
                loadMapLayer(LayerType.BACKGROUND, fileName),
                loadMapLayer(LayerType.MIDDLE, fileName));
    }
    
    private MapLayerInfo loadMapLayer(LayerType layer, String fileName) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
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

    private BackgroundPart getPartByChar(char c, List<BackgroundPart> parts) {
        return parts.stream().filter(x -> x.getCharacter() == c).findFirst().orElse(null);
    }
    
}
