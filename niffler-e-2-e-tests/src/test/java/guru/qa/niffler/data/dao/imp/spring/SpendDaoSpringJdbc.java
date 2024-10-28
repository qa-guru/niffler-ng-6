package guru.qa.niffler.data.dao.imp.spring;

import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.mapper.SpendEntityRowMapper;
import guru.qa.niffler.data.entity.spend.SpendEntity;
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
    KeyHolder kh = new GeneratedKeyHolder();

    new JdbcTemplate(dataSource).update(con -> {
      PreparedStatement ps = con.prepareStatement(
          "INSERT INTO spend (username, spend_date, currency, amount, description, category_id) " +
              "VALUES(?, ?, ?, ?, ?, ?)",
          Statement.RETURN_GENERATED_KEYS
      );
      ps.setString(1, spend.getUsername());
      ps.setDate(2, spend.getSpendDate());
      ps.setString(3, spend.getCurrency().name());
      ps.setDouble(4, spend.getAmount());
      ps.setString(5, spend.getDescription());
      ps.setObject(6, spend.getCategory().getId());
      return ps;
    }, kh);

    final UUID generatedKey = (UUID) kh.getKeys().get("id");
    spend.setId(generatedKey);
    return spend;
  }

  @Override
  public Optional<SpendEntity> findById(UUID id) {
    return Optional.ofNullable(
        new JdbcTemplate(dataSource).queryForObject(
            "SELECT * FROM spend WHERE id = ?",
            SpendEntityRowMapper.instance,
            id
        )
    );
  }

  @Override
  public List<SpendEntity> findAll() {
    return new JdbcTemplate(dataSource).query(
        "SELECT * FROM spend",
        SpendEntityRowMapper.instance
    );
  }

  @Override
  public void deleteById(UUID id) {
    new JdbcTemplate(dataSource).query(
        "DELETE FROM spend WHERE id = ?",
        SpendEntityRowMapper.instance,
        id
    );
  }
}
