package guru.qa.niffler.service;

import guru.qa.niffler.model.AuthUserJson;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthUserClient {

    AuthUserJson create(@Nonnull AuthUserJson authUserJson);

    Optional<AuthUserJson> findById(@Nonnull UUID id);

    Optional<AuthUserJson> findByUsername(@Nonnull String username);

    List<AuthUserJson> findAll();

    void remove(@Nonnull AuthUserJson authUser);

}
