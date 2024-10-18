package guru.qa.niffler.service.impl.jdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.jdbc.CategoryDaoJdbc;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.mapper.CategoryMapper;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.service.CategoryDbClient;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class CategoryDbClientJdbc implements CategoryDbClient {

    private static final String SPEND_JDBC_URL = Config.getInstance().spendJdbcUrl();
    private final CategoryMapper categoryMapper = new CategoryMapper();

    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(SPEND_JDBC_URL);

    @Override
    public CategoryJson create(CategoryJson categoryJson) {
        log.info("Creating new category by DTO: {}", categoryJson);
        return jdbcTxTemplate.execute(() ->
                    categoryMapper.toDto(
                            new CategoryDaoJdbc().create(
                                    categoryMapper.toEntity(categoryJson))));
    }

    @Override
    public Optional<CategoryJson> findById(UUID id) {
        log.info("Get category by id = [{}]", id);
        return jdbcTxTemplate.execute(() ->
                    new CategoryDaoJdbc()
                            .findById(id)
                            .map(categoryMapper::toDto));
    }

    @Override
    public Optional<CategoryJson> findByUsernameAndName(String username, String name) {
        log.info("Get category by username = [{}] and name = [{}]", username, name);
        return jdbcTxTemplate.execute(() ->
            new CategoryDaoJdbc()
                    .findByUsernameAndName(username, name)
                    .map(categoryMapper::toDto)
        );
    }

    @Override
    public List<CategoryJson> findAllByUsername(String username) {
        log.info("Get categories by username = [{}]", username);
        return jdbcTxTemplate.execute(() ->
            new CategoryDaoJdbc()
                    .findAllByUsername(username).stream()
                    .map(categoryMapper::toDto)
                    .toList()
        );
    }

    @Override
    public List<CategoryJson> findAll() {
        log.info("Get all categories");
        return jdbcTxTemplate.execute(() ->
                new CategoryDaoJdbc()
                        .findAll().stream()
                        .map(categoryMapper::toDto)
                        .toList()
        );
    }

    @Override
    public void delete(CategoryJson categoryJson) {
        log.info("Remove category: {}", categoryJson);
        jdbcTxTemplate.execute(() -> {
            new CategoryDaoJdbc().delete(categoryMapper.toEntity(categoryJson));
            return null;
        });
    }

}
