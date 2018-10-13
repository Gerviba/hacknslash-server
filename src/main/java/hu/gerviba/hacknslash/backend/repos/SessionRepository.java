package hu.gerviba.hacknslash.backend.repos;

import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import hu.gerviba.hacknslash.backend.ConfigProfile;
import hu.gerviba.hacknslash.backend.model.SessionUserEntity;

@Profile(ConfigProfile.AUTH_SERVER)
@Repository
public interface SessionRepository extends CrudRepository<SessionUserEntity, String> {

    Optional<SessionUserEntity> findBySessionId(String sessionId);
    
}
