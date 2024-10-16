package guru.qa.niffler.service.jdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.impl.jdbc.CategoryDaoJdbc;
import guru.qa.niffler.mapper.CategoryMapper;
import guru.qa.niffler.model.CategoryJson;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.transaction;

@Slf4j
public class CategoryDbClient {

    private static final String SPEND_JDBC_URL = Config.getInstance().spendJdbcUrl();
    private static final int TRANSACTION_ISOLATION_LEVEL = Connection.TRANSACTION_READ_COMMITTED;
    private final CategoryMapper categoryMapper = new CategoryMapper();

    public CategoryJson create(CategoryJson categoryJson) {
        log.info("Creating new category by DTO: {}", categoryJson);
        return transaction(connection -> {
                    return categoryMapper.toDto(
                            new CategoryDaoJdbc(connection).create(
                                    categoryMapper.toEntity(categoryJson)));
                },
                SPEND_JDBC_URL,
                TRANSACTION_ISOLATION_LEVEL
        );
    }

    public Optional<CategoryJson> findById(UUID id) {
        log.info("Get category by id = [{}]", id);
        return transaction(connection -> {
                    return new CategoryDaoJdbc(connection)
                            .findById(id)
                            .map(categoryMapper::toDto);
                },
                SPEND_JDBC_URL,
                TRANSACTION_ISOLATION_LEVEL
        );
    }

    public Optional<CategoryJson> findByUsernameAndName(String username, String name) {
        log.info("Get category by username = [{}] and name = [{}]", username, name);
        return transaction(connection -> {
                    return new CategoryDaoJdbc(connection)
                            .findByUsernameAndName(username, name)
                            .map(categoryMapper::toDto);
                },
                SPEND_JDBC_URL,
                TRANSACTION_ISOLATION_LEVEL
        );
    }

    public List<CategoryJson> findAllByUsername(String username) {
        log.info("Get categories by username = [{}]", username);
        return transaction(connection -> {
                    return new CategoryDaoJdbc(connection)
                            .findAllByUsername(username).stream()
                            .map(categoryMapper::toDto)
                            .toList();
                },
                SPEND_JDBC_URL,
                TRANSACTION_ISOLATION_LEVEL
        );
    }

    public void delete(UUID id) {
        log.info("Remove category by id = [{}]", id);
        transaction(connection -> {
                    CategoryDao categoryDao = new CategoryDaoJdbc(connection);
                    categoryDao.findById(id).ifPresent(categoryDao::delete);
                    return null;
                },
                SPEND_JDBC_URL,
                TRANSACTION_ISOLATION_LEVEL
        );
    }

}
