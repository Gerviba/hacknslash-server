package hu.gerviba.hacknslash.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import hu.gerviba.hacknslash.backend.ConfigProfile;
import hu.gerviba.hacknslash.backend.repos.PlayerRepository;

@Service
@Profile(ConfigProfile.GAME_SERVER)
public class PlayerService {

    @Autowired
    PlayerRepository players;
    
    
}
