package guru.qa.niffler.data.dao.impl.springJdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.rowMapper.CategoryRowMapper;
import guru.qa.niffler.data.tpl.DataSources;
import lombok.NonNull;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CategoryDaoSpringJdbc implements CategoryDao {

    private static final String SPEND_JDBC_URL = Config.getInstance().spendJdbcUrl();

    public CategoryEntity create(@NonNull CategoryEntity category) {

        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(SPEND_JDBC_URL));
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {

                    PreparedStatement ps = connection.prepareStatement(
                            "INSERT INTO \"category\" (name, username, archived) VALUES (?, ?, ?)",
                            Statement.RETURN_GENERATED_KEYS
                    );
                    ps.setString(1, category.getName());
                    ps.setString(2, category.getUsername());
                    ps.setBoolean(3, category.isArchived());
                    return ps;

                },
                keyHolder

        );

        final UUID generatedKey = (UUID) keyHolder.getKeys().get("id");
        category.setId(generatedKey);
        return category;

    }

    public Optional<CategoryEntity> findById(@NonNull UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(SPEND_JDBC_URL));
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "SELECT * FROM \"category\" WHERE id = ?",
                        CategoryRowMapper.INSTANCE,
                        id
                ));
    }

    @Override
    public Optional<CategoryEntity> findByUsernameAndName(@NonNull String username, @NonNull String categoryName) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(SPEND_JDBC_URL));
        try {
            // QueryForObject not returns null if not found object. Method throws EmptyResultDataAccessException
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            "SELECT * FROM \"category\" WHERE username = ? AND name = ?",
                            CategoryRowMapper.INSTANCE,
                            username,
                            categoryName
                    ));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<CategoryEntity> findAllByUsername(@NonNull String username) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(SPEND_JDBC_URL));
        return jdbcTemplate.query(
                "SELECT * FROM \"category\" WHERE username = ?",
                CategoryRowMapper.INSTANCE,
                username
        );
    }

    @Override
    public List<CategoryEntity> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(SPEND_JDBC_URL));
        return jdbcTemplate.query(
                "SELECT * FROM \"category\"",
                CategoryRowMapper.INSTANCE
        );
    }

    public CategoryEntity update(@NonNull CategoryEntity category) {

        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(SPEND_JDBC_URL));
        jdbcTemplate.update(connection -> {

                    PreparedStatement ps = connection.prepareStatement(
                            "UPDATE \"category\" SET name = ?, username = ?, archived = ? WHERE id = ?"
                    );
                    ps.setString(1, category.getName());
                    ps.setString(2, category.getUsername());
                    ps.setBoolean(3, category.isArchived());
                    ps.setObject(4, category.getId());
                    return ps;

                }
        );

        return category;

    }

    @Override
    public void remove(@NonNull CategoryEntity category) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(SPEND_JDBC_URL));
        jdbcTemplate.update(
                "DELETE FROM \"category\" WHERE id = ?",
                category.getId()
        );
    }

}
