package guru.qa.niffler.data.dao.imp.spring;

import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.mapper.AuthAuthorityEntityRowMapper;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthAuthorityDaoSpringJdbc implements AuthAuthorityDao {

  private final DataSource dataSource;

  public AuthAuthorityDaoSpringJdbc(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public void create(AuthAuthorityEntity... authority) {
    new JdbcTemplate(dataSource).batchUpdate(
        "INSERT INTO authority (user_id, authority) VALUES (?, ?)",
        new BatchPreparedStatementSetter() {
          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setObject(1, authority[i].getUserId());
            ps.setString(2, authority[i].getAuthority().name());
          }

          @Override
          public int getBatchSize() {
            return authority.length;
          }
        }
    );
  }

  @Override
  public Optional<AuthAuthorityEntity> findById(UUID id) {
    return Optional.ofNullable(
        new JdbcTemplate(dataSource).queryForObject(
            "SELECT * FROM authority WHERE id = ?",
            AuthAuthorityEntityRowMapper.instance,
            id
        )
    );
  }

  @Override
  public List<AuthAuthorityEntity> findAll() {
    return new JdbcTemplate(dataSource).query(
        "SELECT * FROM authority",
        AuthAuthorityEntityRowMapper.instance
    );
  }

  @Override
  public void deleteById(UUID id) {
    new JdbcTemplate(dataSource).queryForObject(
        "DELETE FROM authority WHERE id = ?",
        AuthAuthorityEntityRowMapper.instance,
        id
    );
  }
}
