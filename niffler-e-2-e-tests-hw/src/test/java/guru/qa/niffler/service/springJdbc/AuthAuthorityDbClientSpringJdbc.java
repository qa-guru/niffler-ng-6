package guru.qa.niffler.service.springJdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.springJdbc.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.data.entity.auth.AuthAuthorityJson;
import guru.qa.niffler.mapper.AuthAuthorityMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.dataSource;

@Slf4j
public class AuthAuthorityDbClientSpringJdbc {

    private static final String AUTH_JDBC_URL = Config.getInstance().authJdbcUrl();
    private final AuthAuthorityMapper authorityMapper = new AuthAuthorityMapper();


    public void create(AuthAuthorityJson... authorities) {
        log.info("Create new authorities: {}", Arrays.toString(authorities));
        new AuthAuthorityDaoSpringJdbc(dataSource(AUTH_JDBC_URL))
                .create(
                        Arrays.stream(authorities)
                                .map(authorityMapper::toEntity)
                                .toArray(AuthAuthorityEntity[]::new)
                );
    }

    public Optional<AuthAuthorityJson> findById(UUID id) {
        log.info("Get authority by id = [{}]", id);
        return new AuthAuthorityDaoSpringJdbc(dataSource(AUTH_JDBC_URL))
                .findById(id)
                .map(authorityMapper::toDto);
    }

    public List<AuthAuthorityJson> findByUserId(UUID userId) {
        log.info("Get authorities by user id = [{}]", userId);
        return new AuthAuthorityDaoSpringJdbc(dataSource(AUTH_JDBC_URL))
                .findByUserId(userId).stream()
                .map(authorityMapper::toDto)
                .toList();
    }

    public List<AuthAuthorityJson> findAll() {
        log.info("Get all authorities");
        return new AuthAuthorityDaoSpringJdbc(dataSource(AUTH_JDBC_URL))
                .findAll().stream()
                .map(authorityMapper::toDto)
                .toList();
    }

    public void delete(AuthAuthorityJson... authorities) {
        log.info("Remove authorities: {}", Arrays.toString(authorities));
        new AuthAuthorityDaoSpringJdbc(dataSource(AUTH_JDBC_URL))
                .delete(
                        Arrays.stream(authorities)
                                .map(authorityMapper::toEntity)
                                .toArray(AuthAuthorityEntity[]::new)
                );
    }

}
