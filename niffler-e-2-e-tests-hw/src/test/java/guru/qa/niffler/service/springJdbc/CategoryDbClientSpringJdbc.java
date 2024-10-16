package guru.qa.niffler.service.springJdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.mapper.CategoryMapper;
import guru.qa.niffler.model.CategoryJson;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.transaction;

@Slf4j
public class CategoryDbClientSpringJdbc {

    private static final String SPEND_JDBC_URL = Config.getInstance().spendJdbcUrl();
    private static final int TRANSACTION_ISOLATION_LEVEL = Connection.TRANSACTION_READ_COMMITTED;
    private final CategoryMapper categoryMapper = new CategoryMapper();

    public CategoryJson create(CategoryJson categoryJson) {
        log.info("Creating new category by DTO: {}", categoryJson);
        return null;
    }

    public Optional<CategoryJson> findById(UUID id) {
        log.info("Get category by id = [{}]", id);
        return Optional.empty();
    }

    public Optional<CategoryJson> findByUsernameAndName(String username, String name) {
        log.info("Get category by username = [{}] and name = [{}]", username, name);
        return Optional.empty();
    }

    public List<CategoryJson> findAllByUsername(String username) {
        log.info("Get categories by username = [{}]", username);
        return null;
    }

    public void delete(UUID id) {
        log.info("Remove category by id = [{}]", id);
    }

}
