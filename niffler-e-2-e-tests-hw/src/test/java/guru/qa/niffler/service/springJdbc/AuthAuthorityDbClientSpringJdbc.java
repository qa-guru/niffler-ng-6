package guru.qa.niffler.service.springJdbc;

import guru.qa.niffler.config.Config;
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
public class AuthAuthorityDbClientSpringJdbc {

    private static final String AUTH_JDBC_URL = Config.getInstance().authJdbcUrl();
    private static final int TRANSACTION_ISOLATION_LEVEL = Connection.TRANSACTION_READ_COMMITTED;
    private final AuthAuthorityMapper authorityMapper = new AuthAuthorityMapper();

    public void create(AuthAuthorityJson... authorities) {
        log.info("Create new authorities: {}", Arrays.toString(authorities));
    }

    public Optional<AuthAuthorityJson> findById(UUID id) {
        log.info("Get authority by id = [{}]", id);
        return Optional.empty();
    }

    public List<AuthAuthorityJson> findByUserId(UUID userId) {
        log.info("Get authorities by user id = [{}]", userId);
        return null;
    }

    public void delete(AuthAuthorityJson... authorities) {
        log.info("Remove authorities: {}", Arrays.toString(authorities));

    }

}
