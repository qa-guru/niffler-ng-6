package guru.qa.niffler.service.db.impl.springJdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.repository.impl.springJdbc.AuthUserRepositorySpringJdbc;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.mapper.AuthUserMapper;
import guru.qa.niffler.model.rest.AuthUserJson;
import guru.qa.niffler.service.db.AuthUserDbClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.ParametersAreNullableByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@ParametersAreNullableByDefault
public class AuthUserDbClientSpringJdbc implements AuthUserDbClient {

    private static final String AUTH_JDBC_URL = Config.getInstance().authJdbcUrl();
    private final AuthUserRepositorySpringJdbc authUserRepository = new AuthUserRepositorySpringJdbc();
    private final TransactionTemplate txTemplate = new TransactionTemplate(
            new JdbcTransactionManager(DataSources.dataSource(AUTH_JDBC_URL)));
    private final AuthUserMapper authUserMapper = new AuthUserMapper();

    @Override
    public AuthUserJson create(AuthUserJson authUserJson) {
        log.info("Creating new user by DTO: {}", authUserJson);
        return txTemplate.execute(status ->
                authUserMapper.toDto(
                        authUserRepository
                                .create(authUserMapper.toEntity(authUserJson))));
    }

    @Override
    public Optional<AuthUserJson> findById(UUID id) {
        log.info("Get user by id = [{}]", id);
        return txTemplate.execute(status ->
                authUserRepository
                        .findById(id)
                        .map(authUserMapper::toDto));
    }

    @Override
    public Optional<AuthUserJson> findByUsername(String username) {
        log.info("Get user by username = [{}]", username);
        return txTemplate.execute(status ->
                authUserRepository
                        .findByUsername(username)
                        .map(authUserMapper::toDto));
    }

    @Override
    public List<AuthUserJson> findAll() {
        log.info("Get all auth users");
        return txTemplate.execute(status ->
                authUserRepository
                        .findAll().stream()
                        .map(authUserMapper::toDto)
                        .toList());
    }

    @Override
    public void remove(AuthUserJson authUser) {
        log.info("Remove user: {}", authUser);
        txTemplate.execute(status -> {
            authUserRepository
                    .remove(authUserMapper.toEntity(authUser));
            return null;
        });
    }

    @Override
    public void removeAll() {
        log.info("Remove all users");
        txTemplate.execute(status -> {
            authUserRepository.removeAll();
            return null;
        });
    }

}
