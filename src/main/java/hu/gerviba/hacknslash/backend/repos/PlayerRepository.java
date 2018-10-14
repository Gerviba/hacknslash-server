package hu.gerviba.hacknslash.backend.repos;

import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import hu.gerviba.hacknslash.backend.ConfigProfile;
import hu.gerviba.hacknslash.backend.model.PlayerEntity;

@Profile(ConfigProfile.GAME_SERVER)
@Repository
public interface PlayerRepository extends CrudRepository<PlayerEntity, String> {

}
