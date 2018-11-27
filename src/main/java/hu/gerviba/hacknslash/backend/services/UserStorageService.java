package hu.gerviba.hacknslash.backend.services;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PreDestroy;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import hu.gerviba.hacknslash.backend.ConfigProfile;
import hu.gerviba.hacknslash.backend.model.PlayerEntity;
import hu.gerviba.hacknslash.backend.pojo.game.IngameMap;
import hu.gerviba.hacknslash.backend.repos.PlayerRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * User and map storage service
 * @author Gergely Szabó
 */
@Slf4j
@Profile(ConfigProfile.GAME_SERVER)
@Service
public class UserStorageService {

    private Map<String, PlayerEntity> players = new ConcurrentHashMap<>();
    
    @Autowired
    ConcurrentHashMap<String, IngameMap> maps;
    
    @Autowired
    PlayerRepository repo;
    
    /**
     * Player getter by session ID
     * @param sessionId Session ID of the user
     * @return PlayerEntity instance
     */
    public PlayerEntity getPlayer(String sessionId) {
        return players.get(sessionId);
    }
    
    /**
     * Map getter
     * @param storeName Store name of the map
     * @return The selected map
     */
    public IngameMap getMap(String storeName) {
        return maps.get(storeName);
    }
    
    /**
     * Append player to the storage
     * @param sessionId Session ID of the player
     * @param player The PlayerEntity instance
     */
    public void addPlayer(String sessionId, PlayerEntity player) {
        players.put(sessionId, player);
    }
    
    /**
     * Save and remove the player from the storage
     * @param sessionId Session ID of the user
     */
    @Transactional
    public void removePlayer(String sessionId) {
        PlayerEntity player = players.get(sessionId);
        maps.get(player.getMap()).removePlayer(player);        
        repo.save(players.remove(sessionId));
    }
    
    public int countPlayers() {
        return players.size();
    }
    
    /**
     * @return All the players
     */
    public Collection<PlayerEntity> getAll() {
        return players.values();
    }

    /**
     * @return All the maps
     */
    public Collection<IngameMap> getMaps() {
        return maps.values();
    }
    
    /**
     * Respawn timer
     */
    @Scheduled(fixedRate = 1000)
    public void tickRespawnTimer() {
        maps.values().forEach(IngameMap::spawnTimerTicks);
    }
    
    /**
     * Recalculate route
     */
    @Scheduled(fixedRate = 8000)
    public void tickRecaluclatePath() {
        maps.values().forEach(IngameMap::recalculatePaths);
    }
    
    /**
     * Save and clean the storage
     */
    @PreDestroy
    public void destroy() {
        log.info("Saving and removing " + players.size() + " player(s)");
        players.values().stream().forEach(x -> removePlayer(x.getSessionId()));
    }
    
}
