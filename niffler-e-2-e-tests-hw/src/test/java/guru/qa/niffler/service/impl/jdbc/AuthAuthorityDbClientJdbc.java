package guru.qa.niffler.service.impl.jdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.impl.jdbc.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.data.entity.auth.AuthAuthorityJson;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.mapper.AuthAuthorityMapper;
import guru.qa.niffler.service.AuthAuthorityDbClient;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Slf4j
public class AuthAuthorityDbClientJdbc implements AuthAuthorityDbClient {

    private static final String AUTH_JDBC_URL = Config.getInstance().authJdbcUrl();
    private final AuthAuthorityMapper authorityMapper = new AuthAuthorityMapper();

    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(AUTH_JDBC_URL);

    @Override
    public void create(AuthAuthorityJson... authorities) {
        log.info("Create new authorities: {}", Arrays.toString(authorities));
        jdbcTxTemplate.execute(() -> {
                    AuthAuthorityDao authorityDao = new AuthAuthorityDaoJdbc();
                    authorityDao.create(Arrays.stream(authorities)
                            .map(authorityMapper::toEntity)
                            .toArray(AuthAuthorityEntity[]::new));
                    return null;
                }
        );
    }

    @Override
    public Optional<AuthAuthorityJson> findById(UUID id) {
        log.info("Get authority by id = [{}]", id);
        return jdbcTxTemplate.execute(() ->
                        new AuthAuthorityDaoJdbc()
                                .findById(id)
                                .map(authorityMapper::toDto)
                
        );
    }

    @Override
    public List<AuthAuthorityJson> findByUserId(UUID userId) {
        log.info("Get authorities by user id = [{}]", userId);
        return jdbcTxTemplate.execute(() ->
                        new AuthAuthorityDaoJdbc()
                                .findByUserId(userId)
                                .stream()
                                .map(authorityMapper::toDto)
                                .toList()
        );
    }

    @Override
    public List<AuthAuthorityJson> findAll() {
        log.info("Get all authorities");
        return jdbcTxTemplate.execute(() ->
                        new AuthAuthorityDaoJdbc()
                                .findAll().stream()
                                .map(authorityMapper::toDto)
                                .toList()
        );
    }

    @Override
    public void delete(AuthAuthorityJson... authorities) {
        log.info("Remove authorities: {}", Arrays.toString(authorities));
        jdbcTxTemplate.execute(() -> {
                    AuthAuthorityDao authorityDao = new AuthAuthorityDaoJdbc();
                    authorityDao.delete(
                            Arrays.stream(authorities)
                                    .map(authorityMapper::toEntity)
                                    .toArray(AuthAuthorityEntity[]::new)
                    );
                    return null;
                }
        );
    }

}
