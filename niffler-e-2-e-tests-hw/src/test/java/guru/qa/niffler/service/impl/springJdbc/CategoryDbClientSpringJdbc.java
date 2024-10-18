package guru.qa.niffler.service.impl.springJdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.springJdbc.CategoryDaoSpringJdbc;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.mapper.CategoryMapper;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.service.CategoryDbClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class CategoryDbClientSpringJdbc implements CategoryDbClient {

    private static final String SPEND_JDBC_URL = Config.getInstance().spendJdbcUrl();
    private final TransactionTemplate txTemplate = new TransactionTemplate(
            new JdbcTransactionManager(DataSources.dataSource(SPEND_JDBC_URL)));
    private final CategoryMapper categoryMapper = new CategoryMapper();

    @Override
    public CategoryJson create(CategoryJson categoryJson) {
        log.info("Creating new category by DTO: {}", categoryJson);
        return txTemplate.execute(status ->
                categoryMapper.toDto(new CategoryDaoSpringJdbc()
                        .create(categoryMapper.toEntity(categoryJson))));
    }

    @Override
    public Optional<CategoryJson> findById(UUID id) {
        log.info("Get category by id = [{}]", id);
        return txTemplate.execute(status ->
                new CategoryDaoSpringJdbc()
                        .findById(id).map(categoryMapper::toDto));
    }

    @Override
    public Optional<CategoryJson> findByUsernameAndName(String username, String name) {
        log.info("Get category by username = [{}] and name = [{}]", username, name);
        return txTemplate.execute(status ->
                new CategoryDaoSpringJdbc()
                        .findByUsernameAndName(username, name).map(categoryMapper::toDto));
    }

    @Override
    public List<CategoryJson> findAllByUsername(String username) {
        log.info("Get categories by username = [{}]", username);
        return txTemplate.execute(status ->
                new CategoryDaoSpringJdbc()
                        .findAllByUsername(username).stream()
                        .map(categoryMapper::toDto)
                        .toList());
    }

    @Override
    public List<CategoryJson> findAll() {
        log.info("Get all categories");
        return txTemplate.execute(status ->
                new CategoryDaoSpringJdbc()
                        .findAll().stream()
                        .map(categoryMapper::toDto)
                        .toList());
    }

    @Override
    public void delete(CategoryJson categoryJson) {
        log.info("Remove category: {}", categoryJson);
        txTemplate.execute(status -> {
            new CategoryDaoSpringJdbc()
                    .delete(categoryMapper.toEntity(categoryJson));
            return null;
        });
    }

}
