package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.mapper.SpendEntityRomManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDaoSpringJdbc implements SpendDao {

    private final DataSource dataSource;

    public SpendDaoSpringJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public SpendEntity create(SpendEntity spend) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update( con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO spend (username, spend_date, currency, amount, description, category_id)" +
                            "VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1,spend.getUsername());
            ps.setDate(2, spend.getSpendDate());
            ps.setObject(3, spend.getCurrency());
            ps.setDouble(4, spend.getAmount());
            ps.setString(5, spend.getDescription());
            ps.setObject(6, spend.getCategory().getId());
            return ps;
        }, kh);
        final UUID generatednKey = (UUID) kh.getKeys().get("id");
        spend.setId(generatednKey);
        return spend;
    }

    @Override
    public Optional<SpendEntity> findSpendById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "SELECT * FROM spend WHERE id = ?",
                        SpendEntityRomManager.instance,
                        id
                )
        );
    }

    @Override
    public List<SpendEntity> findAllByUsername(String username) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        return
                jdbcTemplate.query(
                        "SELECT * FROM spend WHERE username = ?",
                        SpendEntityRomManager.instance,
                        username
                );
    }

    @Override
    public List<SpendEntity> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        return jdbcTemplate.query(
                "SELECT * FROM spend WHERE username = ?",
                SpendEntityRomManager.instance
        );
    }

    @Override
    public void deleteSpend(SpendEntity spend) {
        new JdbcTemplate().update(
                "DELETE FROM spend WHERE id = ?",
                spend.getId()
        );
    }
}
