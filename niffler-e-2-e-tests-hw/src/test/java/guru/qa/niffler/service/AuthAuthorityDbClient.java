package guru.qa.niffler.service;

import guru.qa.niffler.data.entity.auth.AuthAuthorityJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthAuthorityDbClient {

    void create(AuthAuthorityJson... authorities);

    Optional<AuthAuthorityJson> findById(UUID id);

    List<AuthAuthorityJson> findByUserId(UUID userId);

    List<AuthAuthorityJson> findAll();

    void delete(AuthAuthorityJson... authorities);

}
