package hu.gerviba.hacknslash.backend.services;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import hu.gerviba.hacknslash.backend.ConfigProfile;
import hu.gerviba.hacknslash.backend.model.PlayerEntity;
import hu.gerviba.hacknslash.backend.pojo.game.IngameMap;
import hu.gerviba.hacknslash.backend.repos.PlayerRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile(ConfigProfile.GAME_SERVER)
@Service
public class UserStorageService {

    private Map<String, PlayerEntity> players = new ConcurrentHashMap<>();
    
    @Autowired
    ConcurrentHashMap<String, IngameMap> maps;
    
    @Autowired
    PlayerRepository repo;
    
    public PlayerEntity getPlayer(String sessionId) {
        return players.get(sessionId);
    }
    
    public IngameMap getMap(String storeName) {
        return maps.get(storeName);
    }
    
    public void addPlayer(String sessionId, PlayerEntity player) {
        players.put(sessionId, player);
    }
    
    public void removePlayer(String sessionId) {
        PlayerEntity player = players.get(sessionId);
        maps.get(player.getMap()).removePlayer(player);        
        repo.save(players.remove(sessionId));
    }
    
    public int countPlayers() {
        return players.size();
    }
    
    public Collection<PlayerEntity> getAll() {
        return players.values();
    }

    public Collection<IngameMap> getMaps() {
        return maps.values();
    }
    
    @Scheduled(fixedRate = 1000)
    public void tickRespawnTimer() {
        maps.values().forEach(IngameMap::spawnTimerTicks);
    }
    
    @Scheduled(fixedRate = 8000)
    public void tickRecaluclatePath() {
        maps.values().forEach(IngameMap::recalculatePaths);
    }
    
    @PreDestroy
    public void destroy() {
        log.info("Saving and removing " + players.size() + " player(s)");
        players.values().stream().forEach(x -> removePlayer(x.getSessionId()));
    }
    
}
