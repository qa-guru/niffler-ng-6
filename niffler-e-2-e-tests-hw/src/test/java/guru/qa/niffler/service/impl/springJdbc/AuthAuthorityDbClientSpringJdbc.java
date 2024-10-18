package guru.qa.niffler.service.impl.springJdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.springJdbc.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.data.entity.auth.AuthAuthorityJson;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.mapper.AuthAuthorityMapper;
import guru.qa.niffler.service.AuthAuthorityDbClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class AuthAuthorityDbClientSpringJdbc implements AuthAuthorityDbClient {

    private static final String AUTH_JDBC_URL = Config.getInstance().authJdbcUrl();
    private final TransactionTemplate txTemplate = new TransactionTemplate(
            new JdbcTransactionManager(DataSources.dataSource(AUTH_JDBC_URL)));
    private final AuthAuthorityMapper authorityMapper = new AuthAuthorityMapper();


    @Override
    public void create(AuthAuthorityJson... authorities) {
        log.info("Create new authorities: {}", Arrays.toString(authorities));
        txTemplate.execute(status -> {
            new AuthAuthorityDaoSpringJdbc()
                    .create(
                            Arrays.stream(authorities)
                                    .map(authorityMapper::toEntity)
                                    .toArray(AuthAuthorityEntity[]::new)
                    );
            return null;
        });
    }

    @Override
    public Optional<AuthAuthorityJson> findById(UUID id) {
        log.info("Get authority by id = [{}]", id);
        return txTemplate.execute(status ->
                new AuthAuthorityDaoSpringJdbc()
                        .findById(id)
                        .map(authorityMapper::toDto));
    }

    @Override
    public List<AuthAuthorityJson> findByUserId(UUID userId) {
        log.info("Get authorities by user id = [{}]", userId);
        return txTemplate.execute(status ->
                new AuthAuthorityDaoSpringJdbc()
                        .findByUserId(userId).stream()
                        .map(authorityMapper::toDto)
                        .toList());
    }

    @Override
    public List<AuthAuthorityJson> findAll() {
        log.info("Get all authorities");
        return txTemplate.execute(status ->
                new AuthAuthorityDaoSpringJdbc()
                        .findAll().stream()
                        .map(authorityMapper::toDto)
                        .toList());
    }

    @Override
    public void delete(AuthAuthorityJson... authorities) {
        log.info("Remove authorities: {}", Arrays.toString(authorities));
        txTemplate.execute(status -> {
            new AuthAuthorityDaoSpringJdbc()
                    .delete(
                            Arrays.stream(authorities)
                                    .map(authorityMapper::toEntity)
                                    .toArray(AuthAuthorityEntity[]::new)
                    );
            return null;
        });
    }

}
