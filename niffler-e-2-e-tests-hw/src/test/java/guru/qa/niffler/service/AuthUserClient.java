package guru.qa.niffler.service;

import guru.qa.niffler.model.AuthUserJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthUserClient {

    AuthUserJson create(AuthUserJson authUserJson);

    Optional<AuthUserJson> findById(UUID id);

    Optional<AuthUserJson> findByUsername(String username);

    List<AuthUserJson> findAll();

    void remove(AuthUserJson authUser);

}
