package guru.qa.niffler.service;

import guru.qa.niffler.data.entity.auth.AuthUserJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthUserDbClient {

    AuthUserJson create(AuthUserJson authUserJson);

    Optional<AuthUserJson> findById(UUID id);

    Optional<AuthUserJson> findByUsername(String username);

    List<AuthUserJson> findAll();

    void delete(AuthUserJson authUser);

}
