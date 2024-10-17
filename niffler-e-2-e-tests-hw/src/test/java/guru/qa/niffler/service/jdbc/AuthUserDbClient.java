package guru.qa.niffler.service.jdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.jdbc.AuthUserDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserJson;
import guru.qa.niffler.mapper.AuthUserMapper;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.transaction;

@Slf4j
public class AuthUserDbClient {

    private static final String AUTH_JDBC_URL = Config.getInstance().authJdbcUrl();
    private static final int TRANSACTION_ISOLATION_LEVEL = Connection.TRANSACTION_READ_COMMITTED;
    private final AuthUserMapper authUserMapper = new AuthUserMapper();

    public AuthUserJson create(AuthUserJson userModel) {
        log.info("Creating new user by DTO: {}", userModel);
        return transaction(connection -> {
                        return 
                        authUserMapper.toDto(
                                new AuthUserDaoJdbc(connection)
                                        .create(authUserMapper.toEntity(userModel)));
                },
                AUTH_JDBC_URL,
                TRANSACTION_ISOLATION_LEVEL
        );
    }

    public Optional<AuthUserJson> findById(UUID id) {
        log.info("Get user by id = [{}]", id);
        return transaction(connection -> {
                        return 
                        new AuthUserDaoJdbc(connection)
                                .findById(id)
                                .map(authUserMapper::toDto);
                },
                AUTH_JDBC_URL,
                TRANSACTION_ISOLATION_LEVEL
        );
    }

    public Optional<AuthUserJson> findByUsername(String username) {
        log.info("Get user by username = [{}]", username);
        return transaction(connection -> {
                        return 
                        new AuthUserDaoJdbc(connection)
                                .findByUsername(username)
                                .map(authUserMapper::toDto);
                },
                AUTH_JDBC_URL,
                TRANSACTION_ISOLATION_LEVEL
        );
    }

    public void delete(AuthUserJson authUserJson) {
        log.info("Remove user: {}", authUserJson);
        transaction(connection -> {
                    new AuthUserDaoJdbc(connection).delete(authUserMapper.toEntity(authUserJson));
                },
                AUTH_JDBC_URL,
                TRANSACTION_ISOLATION_LEVEL
        );
    }

}
