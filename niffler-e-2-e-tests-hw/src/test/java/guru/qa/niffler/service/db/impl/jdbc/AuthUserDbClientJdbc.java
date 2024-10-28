package guru.qa.niffler.service.db.impl.jdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.jdbc.AuthUserDaoJdbc;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.mapper.AuthUserMapper;
import guru.qa.niffler.model.AuthUserJson;
import guru.qa.niffler.service.db.AuthUserDbClient;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class AuthUserDbClientJdbc implements AuthUserDbClient {

    private static final String AUTH_JDBC_URL = Config.getInstance().authJdbcUrl();
    private final AuthUserMapper authUserMapper = new AuthUserMapper();

    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(AUTH_JDBC_URL);

    @Override
    public AuthUserJson create(@NonNull AuthUserJson userModel) {
        log.info("Creating new user by DTO: {}", userModel);
        return jdbcTxTemplate.execute(() ->
                authUserMapper.toDto(
                        new AuthUserDaoJdbc()
                                .create(authUserMapper.toEntity(userModel))));

    }

    @Override
    public Optional<AuthUserJson> findById(@NonNull UUID id) {
        log.info("Get user by id = [{}]", id);
        return jdbcTxTemplate.execute(() ->
                new AuthUserDaoJdbc()
                        .findById(id)
                        .map(authUserMapper::toDto));
    }

    @Override
    public Optional<AuthUserJson> findByUsername(@NonNull String username) {
        log.info("Get user by username = [{}]", username);
        return jdbcTxTemplate.execute(() ->
                new AuthUserDaoJdbc()
                        .findByUsername(username)
                        .map(authUserMapper::toDto));
    }

    @Override
    public List<AuthUserJson> findAll() {
        log.info("Get all users");
        return jdbcTxTemplate.execute(() ->
                new AuthUserDaoJdbc()
                        .findAll().stream()
                        .map(authUserMapper::toDto)
                        .toList());
    }

    @Override
    public void remove(@NonNull AuthUserJson authUserJson) {
        log.info("Remove user: {}", authUserJson);
        jdbcTxTemplate.execute(() -> {
                    new AuthUserDaoJdbc()
                            .remove(authUserMapper.toEntity(authUserJson));
                    return null;
                }
        );
    }

}
