package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.mapper.CategoryEntityRowMapper;
import guru.qa.niffler.data.mapper.SpendEntityRowMapper;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendRepositorySpringJdbc implements SpendRepository {

    private static final Config CFG = Config.getInstance();
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));

    @Override
    public SpendEntity create(SpendEntity spend) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO spend (username, spend_date, currency, amount, description, category_id) " +
                            "VALUES (?, ?, ?, ?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, spend.getUsername());
            ps.setDate(2, new java.sql.Date(spend.getSpendDate().getTime()));
            ps.setString(3, spend.getCurrency().name());
            ps.setDouble(4, spend.getAmount());
            ps.setString(5, spend.getDescription());
            ps.setObject(6, spend.getCategory().getId());
            return ps;
        }, keyHolder);

        final UUID generatedKey = (UUID) keyHolder.getKeys().get("id");
        spend.setId(generatedKey);
        return spend;
    }

    @Override
    public SpendEntity update(SpendEntity spend) {
        String updateSql = "UPDATE spend SET spend_date = ?, currency = ?, amount = ?, description = ? WHERE id = ?";
        jdbcTemplate.update(updateSql,
                new java.sql.Date(spend.getSpendDate().getTime()),
                spend.getCurrency().name(),
                spend.getAmount(),
                spend.getDescription(),
                spend.getId()
        );
        return spend;
    }

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO category (username, name, archived) VALUES (?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, category.getUsername());
            ps.setString(2, category.getName());
            ps.setBoolean(3, category.isArchived());
            return ps;
        }, keyHolder);

        final UUID generatedKey = (UUID) keyHolder.getKeys().get("id");
        category.setId(generatedKey);
        return category;
    }

    @Override
    public CategoryEntity updateCategory(CategoryEntity category) {
        String updateSql = "UPDATE category SET name = ?, username = ?, archived = ? WHERE id = ?";
        jdbcTemplate.update(updateSql,
                category.getName(),
                category.getUsername(),
                category.isArchived(),
                category.getId()
        );
        return category;
    }

    @Override
    public Optional<CategoryEntity> findCategoryById(UUID id) {
        String sql = "SELECT * FROM category WHERE id = ?";
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(sql, CategoryEntityRowMapper.instance, id)
        );
    }

    @Override
    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String name) {
        String sql = "SELECT * FROM category WHERE username = ? AND name = ?";
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(sql, CategoryEntityRowMapper.instance, username, name)
        );
    }

    @Override
    public Optional<SpendEntity> findById(UUID id) {
        String sql = "SELECT * FROM spend WHERE id = ?";
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(sql, SpendEntityRowMapper.instance, id)
        );
    }

    @Override
    public List<SpendEntity> findByUsernameAndSpendDescription(String userName, String description) {
        String sql = "SELECT * FROM spend WHERE username = ? AND description = ?";
        return jdbcTemplate.query(sql, SpendEntityRowMapper.instance, userName, description);
    }

    @Override
    public void remove(SpendEntity spend) {
        String sql = "DELETE FROM spend WHERE id = ?";
        jdbcTemplate.update(sql, spend.getId());
    }

    @Override
    public void removeCategory(CategoryEntity category) {
        String sql = "DELETE FROM category WHERE id = ?";
        jdbcTemplate.update(sql, category.getId());
    }
}