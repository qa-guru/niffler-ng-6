package guru.qa.niffler.service.springJdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.springJdbc.CategoryDaoSpringJdbc;
import guru.qa.niffler.mapper.CategoryMapper;
import guru.qa.niffler.model.CategoryJson;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.dataSource;

@Slf4j
public class CategoryDbClientSpringJdbc {

    private static final String SPEND_JDBC_URL = Config.getInstance().spendJdbcUrl();
    private final CategoryMapper categoryMapper = new CategoryMapper();

    public CategoryJson create(CategoryJson categoryJson) {
        log.info("Creating new category by DTO: {}", categoryJson);
        return categoryMapper.toDto(new CategoryDaoSpringJdbc(dataSource(SPEND_JDBC_URL))
                .create(categoryMapper.toEntity(categoryJson)));
    }

    public Optional<CategoryJson> findById(UUID id) {
        log.info("Get category by id = [{}]", id);
        return new CategoryDaoSpringJdbc(dataSource(SPEND_JDBC_URL))
                .findById(id).map(categoryMapper::toDto);
    }

    public Optional<CategoryJson> findByUsernameAndName(String username, String name) {
        log.info("Get category by username = [{}] and name = [{}]", username, name);
        return new CategoryDaoSpringJdbc(dataSource(SPEND_JDBC_URL))
                .findByUsernameAndName(username, name).map(categoryMapper::toDto);
    }

    public List<CategoryJson> findAllByUsername(String username) {
        log.info("Get categories by username = [{}]", username);
        return new CategoryDaoSpringJdbc(dataSource(SPEND_JDBC_URL))
                .findAllByUsername(username).stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    public List<CategoryJson> findAll() {
        log.info("Get all categories");
        return new CategoryDaoSpringJdbc(dataSource(SPEND_JDBC_URL))
                .findAll().stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    public void delete(CategoryJson categoryJson) {
        log.info("Remove category: {}", categoryJson);
        new CategoryDaoSpringJdbc(dataSource(SPEND_JDBC_URL))
                .delete(categoryMapper.toEntity(categoryJson));
    }

}
