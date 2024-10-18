package guru.qa.niffler.service.impl.jdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.jdbc.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.jdbc.SpendDaoJdbc;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.mapper.SpendMapper;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendDbClient;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Slf4j
public class SpendDbClientJdbc implements SpendDbClient {

    private static final String SPEND_JDBC_URL = Config.getInstance().spendJdbcUrl();
    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(SPEND_JDBC_URL);
    private final SpendMapper spendMapper = new SpendMapper();

    @Override
    public SpendJson create(SpendJson spendJson) {

        log.info("Creating new spend by DTO: {}", spendJson);
        return jdbcTxTemplate.execute(() -> {
                    SpendEntity spendEntity = spendMapper.toEntity(spendJson);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity categoryEntity = new CategoryDaoJdbc()
                                .create(spendEntity.getCategory().setUsername(spendJson.getUsername()));
                        spendEntity.setCategory(categoryEntity);
                    }
                    return spendMapper.toDto(new SpendDaoJdbc().create(spendEntity));
                }
        );
    }

    @Override
    public Optional<SpendJson> findById(UUID id) {
        log.info("Get spend by id = [{}]", id);
        return jdbcTxTemplate.execute(() ->
                new SpendDaoJdbc()
                        .findById(id)
                        .map(spendMapper::toDto)
        );
    }

    @Override
    public List<SpendJson> findByUsernameAndDescription(String username, String description) {
        log.info("Get spend by username = [{}] and description = [{}]", username, description);
        return jdbcTxTemplate.execute(() ->
                new SpendDaoJdbc()
                        .findByUsernameAndDescription(username, description).stream()
                        .map(spendMapper::toDto)
                        .toList()
        );
    }

    @Override
    public List<SpendJson> findAllByUsername(String username) {
        log.info("Get all spends by username = [{}]", username);
        return jdbcTxTemplate.execute(() ->
                new SpendDaoJdbc()
                        .findAllByUsername(username).stream()
                        .map(spendMapper::toDto)
                        .toList()
        );
    }

    @Override
    public List<SpendJson> findAll() {
        log.info("Get all spends");
        return jdbcTxTemplate.execute(() ->
                new SpendDaoJdbc()
                        .findAll().stream()
                        .map(spendMapper::toDto)
                        .toList()
        );
    }

    @Override
    public void delete(SpendJson spend) {
        log.info("Remove spend by id: {}", spend);
        jdbcTxTemplate.execute(() -> {
                    new SpendDaoJdbc()
                            .delete(spendMapper.toEntity(spend));
                    return null;
                }
        );
    }

}
