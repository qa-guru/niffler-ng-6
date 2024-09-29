package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.mapper.CategoryEntityRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CategoryDaoSpringJdbc implements CategoryDao {
    private final DataSource dataSource;

    public CategoryDaoSpringJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public CategoryEntity create(CategoryEntity category) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO \"category\" (username, name, archived) " +
                            "VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, category.getUsername());
            ps.setString(2, category.getName());
            ps.setBoolean(3, category.isArchived());
            return ps;
        }, kh);
        final UUID generatedKey = (UUID) kh.getKeys().get("id");
        category.setId(generatedKey);
        return category;
    }

    @Override
    public Optional<CategoryEntity> findCategoryById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "SELECT * FROM \"category\" WHERE id = ?",
                        CategoryEntityRowMapper.instance,
                        id
                )
        );
    }

    @Override
    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName) throws SQLException {
        return Optional.empty();
    }

    @Override
    public List<CategoryEntity> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query("SELECT * FROM \"category\"", CategoryEntityRowMapper.instance);
    }

    @Override
    public List<CategoryEntity> findAllByUsername(String username) {
        return List.of();
    }

    @Override
    public void deleteCategory(CategoryEntity category) {

    }
}