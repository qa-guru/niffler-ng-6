package guru.qa.niffler.service.db.impl.jdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.repository.impl.jdbc.AuthUserRepositoryJdbc;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.mapper.AuthUserMapper;
import guru.qa.niffler.model.AuthUserJson;
import guru.qa.niffler.service.db.AuthUserDbClient;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@ParametersAreNonnullByDefault
public class AuthUserDbClientJdbc implements AuthUserDbClient {

    private static final String AUTH_JDBC_URL = Config.getInstance().authJdbcUrl();
    private final AuthUserMapper authUserMapper = new AuthUserMapper();
    private final AuthUserRepositoryJdbc authUserRepository = new AuthUserRepositoryJdbc();
    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(AUTH_JDBC_URL);

    @Override
    public @Nonnull AuthUserJson create(AuthUserJson userModel) {
        log.info("Creating new user by DTO: {}", userModel);
        return jdbcTxTemplate.execute(() ->
                authUserMapper.toDto(
                        authUserRepository
                                .create(authUserMapper.toEntity(userModel))));

    }

    @Override
    public @Nonnull Optional<AuthUserJson> findById(UUID id) {
        log.info("Get user by id = [{}]", id);
        return jdbcTxTemplate.execute(() ->
                authUserRepository
                        .findById(id)
                        .map(authUserMapper::toDto));
    }

    @Override
    public @Nonnull Optional<AuthUserJson> findByUsername(String username) {
        log.info("Get user by username = [{}]", username);
        return jdbcTxTemplate.execute(() ->
                authUserRepository
                        .findByUsername(username)
                        .map(authUserMapper::toDto));
    }

    @Override
    public @Nonnull List<AuthUserJson> findAll() {
        log.info("Get all users");
        return jdbcTxTemplate.execute(() ->
                authUserRepository
                        .findAll().stream()
                        .map(authUserMapper::toDto)
                        .toList());
    }

    @Override
    public void remove(AuthUserJson authUserJson) {
        log.info("Remove user: {}", authUserJson);
        jdbcTxTemplate.execute(() -> {
                    authUserRepository
                            .remove(authUserMapper.toEntity(authUserJson));
                    return null;
                }
        );
    }

    @Override
    public void removeAll() {
        log.info("Remove all users");
        jdbcTxTemplate.execute(() -> {
                    authUserRepository.removeAll();
                    return null;
                }
        );
    }

}
