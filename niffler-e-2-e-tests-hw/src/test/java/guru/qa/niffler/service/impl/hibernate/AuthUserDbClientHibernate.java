package guru.qa.niffler.service.impl.hibernate;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.springJdbc.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.repository.impl.AuthUserRepositoryJdbc;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.mapper.AuthUserMapper;
import guru.qa.niffler.model.AuthUserJson;
import guru.qa.niffler.service.AuthUserDbClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class AuthUserDbClientHibernate implements AuthUserDbClient {

    private static final String AUTH_JDBC_URL = Config.getInstance().authJdbcUrl();
    private final TransactionTemplate txTemplate = new TransactionTemplate(
            new JdbcTransactionManager(DataSources.dataSource(AUTH_JDBC_URL)));
    private final AuthUserMapper authUserMapper = new AuthUserMapper();

    @Override
    public AuthUserJson create(AuthUserJson authUserJson) {
        log.info("Creating new user by DTO: {}", authUserJson);
        return txTemplate.execute(status ->
                authUserMapper.toDto(
                        new AuthUserRepositoryJdbc()
                                .create(authUserMapper.toEntity(authUserJson))));
    }

    @Override
    public Optional<AuthUserJson> findById(UUID id) {
        log.info("Get user by id = [{}]", id);
        return txTemplate.execute(status ->
                new AuthUserRepositoryJdbc()
                        .findById(id)
                        .map(authUserMapper::toDto));
    }

    @Override
    public Optional<AuthUserJson> findByUsername(String username) {
        log.info("Get user by username = [{}]", username);
        return txTemplate.execute(status ->
                new AuthUserRepositoryJdbc()
                        .findByUsername(username)
                        .map(authUserMapper::toDto));
    }

    @Override
    public List<AuthUserJson> findAll() {
        log.info("Get all auth users");
        return txTemplate.execute(status ->
                new AuthUserRepositoryJdbc()
                        .findAll().stream()
                        .map(authUserMapper::toDto)
                        .toList());
    }

    @Override
    public void delete(AuthUserJson authUser) {
        log.info("Remove user: {}", authUser);
        txTemplate.execute(status -> {
            new AuthUserRepositoryJdbc()
                    .delete(authUserMapper.toEntity(authUser));
            return null;
        });
    }

}
