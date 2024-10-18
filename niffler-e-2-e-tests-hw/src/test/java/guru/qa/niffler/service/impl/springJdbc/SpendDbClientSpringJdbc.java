package guru.qa.niffler.service.impl.springJdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.springJdbc.SpendDaoSpringJdbc;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.mapper.SpendMapper;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendDbClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class SpendDbClientSpringJdbc implements SpendDbClient {

    private static final String SPEND_JDBC_URL = Config.getInstance().spendJdbcUrl();
    private final TransactionTemplate txTemplate = new TransactionTemplate(
            new JdbcTransactionManager(DataSources.dataSource(SPEND_JDBC_URL)));
    private final SpendMapper spendMapper = new SpendMapper();

    @Override
    public SpendJson create(SpendJson spendJson) {
        log.info("Creating new spend by DTO: {}", spendJson);
        return txTemplate.execute(status ->
                spendMapper.toDto(new SpendDaoSpringJdbc()
                        .create(spendMapper.toEntity(spendJson))));
    }

    @Override
    public Optional<SpendJson> findById(UUID id) {
        log.info("Get spend by id = [{}]", id);
        return txTemplate.execute(status ->
                new SpendDaoSpringJdbc()
                        .findById(id).map(spendMapper::toDto));
    }

    @Override
    public List<SpendJson> findByUsernameAndDescription(String username, String description) {
        log.info("Get spend by username = [{}] and name = [{}]", username, description);
        return txTemplate.execute(status ->
                new SpendDaoSpringJdbc()
                        .findByUsernameAndDescription(username, description).stream()
                        .map(spendMapper::toDto)
                        .toList());
    }

    @Override
    public List<SpendJson> findAllByUsername(String username) {
        log.info("Get categories by username = [{}]", username);
        return txTemplate.execute(status ->
                new SpendDaoSpringJdbc()
                        .findAllByUsername(username).stream()
                        .map(spendMapper::toDto)
                        .toList());
    }

    @Override
    public List<SpendJson> findAll() {
        log.info("Get all categories");
        return txTemplate.execute(status ->
                new SpendDaoSpringJdbc()
                        .findAll().stream()
                        .map(spendMapper::toDto)
                        .toList());
    }

    @Override
    public void delete(SpendJson spendJson) {
        log.info("Remove spend: {}", spendJson);
        txTemplate.execute(status -> {
            new SpendDaoSpringJdbc()
                    .delete(spendMapper.toEntity(spendJson));
            return null;
        });
    }

}
