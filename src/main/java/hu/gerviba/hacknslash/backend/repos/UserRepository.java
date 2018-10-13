package hu.gerviba.hacknslash.backend.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import hu.gerviba.hacknslash.backend.ConfigProfile;
import hu.gerviba.hacknslash.backend.model.RegisteredUserEntity;

@Profile(ConfigProfile.AUTH_SERVER)
@Repository
public interface UserRepository extends CrudRepository<RegisteredUserEntity, String> {

    Optional<RegisteredUserEntity> findByEmailIgnoreCase(String email);

    List<RegisteredUserEntity> findAllByUsernameIgnoreCaseOrEmailIgnoreCase(String username, String email);

}
