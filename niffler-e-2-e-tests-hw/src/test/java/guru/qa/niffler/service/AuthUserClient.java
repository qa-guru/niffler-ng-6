package guru.qa.niffler.service;

import guru.qa.niffler.model.AuthUserJson;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthUserClient {

    AuthUserJson create(@NonNull AuthUserJson authUserJson);

    Optional<AuthUserJson> findById(@NonNull UUID id);

    Optional<AuthUserJson> findByUsername(@NonNull String username);

    List<AuthUserJson> findAll();

    void remove(@NonNull AuthUserJson authUser);

}
