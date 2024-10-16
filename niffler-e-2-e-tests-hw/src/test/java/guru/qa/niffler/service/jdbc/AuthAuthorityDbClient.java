package guru.qa.niffler.service.jdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.impl.jdbc.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.data.entity.auth.AuthAuthorityJson;
import guru.qa.niffler.mapper.AuthAuthorityMapper;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.transaction;

@Slf4j
public class AuthAuthorityDbClient {

    private static final String AUTH_JDBC_URL = Config.getInstance().authJdbcUrl();
    private static final int TRANSACTION_ISOLATION_LEVEL = Connection.TRANSACTION_READ_COMMITTED;
    private final AuthAuthorityMapper authorityMapper = new AuthAuthorityMapper();

    public void create(AuthAuthorityJson... authorities) {
        log.info("Create new authorities: {}", Arrays.toString(authorities));
        transaction(connection -> {
                    AuthAuthorityDao authorityDao = new AuthAuthorityDaoJdbc(connection);
                    authorityDao.create(Arrays.stream(authorities)
                            .map(authorityMapper::toEntity)
                            .toArray(AuthAuthorityEntity[]::new));
                },
                AUTH_JDBC_URL,
                TRANSACTION_ISOLATION_LEVEL
        );
    }

    public Optional<AuthAuthorityJson> findById(UUID id) {
        log.info("Get authority by id = [{}]", id);
        return transaction(connection -> {
                    return new AuthAuthorityDaoJdbc(connection)
                            .findById(id)
                            .map(authorityMapper::toDto);
                },
                AUTH_JDBC_URL,
                TRANSACTION_ISOLATION_LEVEL
        );
    }

    public List<AuthAuthorityJson> findByUserId(UUID userId) {
        log.info("Get authorities by user id = [{}]", userId);
        return transaction(connection -> {
                    return new AuthAuthorityDaoJdbc(connection)
                            .findByUserId(userId)
                            .stream()
                            .map(authorityMapper::toDto)
                            .toList();
                },
                AUTH_JDBC_URL,
                TRANSACTION_ISOLATION_LEVEL
        );
    }

    public void delete(AuthAuthorityJson... authorities) {
        log.info("Remove authorities: {}", Arrays.toString(authorities));
        transaction(connection -> {
                    AuthAuthorityDao authorityDao = new AuthAuthorityDaoJdbc(connection);
                    authorityDao.delete(
                            Arrays.stream(authorities)
                                    .map(authorityMapper::toEntity)
                                    .toArray(AuthAuthorityEntity[]::new)
                    );
//                    for (AuthAuthorityJson authorityJson : authorities) {
//                        authorityDao.findById(authorityJson.getId())
//                                .ifPresent(authorityDao::delete);
//                    }
                    return null;
                },
                AUTH_JDBC_URL,
                TRANSACTION_ISOLATION_LEVEL
        );
    }

}
