package hu.gerviba.hacknslash.backend.services;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import hu.gerviba.hacknslash.backend.ConfigProfile;
import hu.gerviba.hacknslash.backend.model.PlayerEntity;
import hu.gerviba.hacknslash.backend.pojo.game.MapPojo;

@Profile(ConfigProfile.GAME_SERVER)
@Service
public class UserStorageService {

    private Map<String, PlayerEntity> players = new ConcurrentHashMap<>();
    
    @Autowired
    private ConcurrentHashMap<String, MapPojo> maps;
    
    public PlayerEntity getPlayer(String sessionId) {
        return players.get(sessionId);
    }
    
    public MapPojo getMap(String storeName) {
        return maps.get(storeName);
    }
    
    public void addPlayer(String sessionId, PlayerEntity player) {
        players.put(sessionId, player);
    }
    
    public void removePlayer(String sessionId) {
        players.remove(sessionId);
    }
    
    public int countPlayers() {
        return players.size();
    }
    
    public Collection<PlayerEntity> getAll() {
        return players.values();
    }

    public Collection<MapPojo> getMaps() {
        return maps.values();
    }
    
}
