package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.jdbc.DataSources;
import guru.qa.niffler.data.mapper.AuthUserEntityRowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class AuthUserDaoSpringJdbc implements AuthUserDao {

  private static final Config CFG = Config.getInstance();
  private final String url = CFG.authJdbcUrl();

  @Nonnull
  @Override
  public AuthUserEntity create(AuthUserEntity user) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(url));
    KeyHolder kh = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(
          """
                 INSERT INTO "user" (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) 
                 VALUES (?,?,?,?,?,?)
              """,
          Statement.RETURN_GENERATED_KEYS
      );
      ps.setString(1, user.getUsername());
      ps.setString(2, user.getPassword());
      ps.setBoolean(3, user.getEnabled());
      ps.setBoolean(4, user.getAccountNonExpired());
      ps.setBoolean(5, user.getAccountNonLocked());
      ps.setBoolean(6, user.getCredentialsNonExpired());
      return ps;
    }, kh);

    final UUID generatedKey = (UUID) kh.getKeys().get("id");
    user.setId(generatedKey);
    return user;
  }

  @Nonnull
  @Override
  public Optional<AuthUserEntity> findById(UUID id) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(url));
    try {
      return Optional.ofNullable(
          jdbcTemplate.queryForObject(
              """
                      SELECT * FROM "user" WHERE id = ?
                  """,
              AuthUserEntityRowMapper.instance,
              id
          )
      );
    } catch (
        EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Nonnull
  @Override
  public Optional<AuthUserEntity> findByUsername(String username) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(url));
    try {
      return Optional.ofNullable(
          jdbcTemplate.queryForObject(
              """
                     SELECT * FROM "user" WHERE username = ?
                  """,
              AuthUserEntityRowMapper.instance,
              username
          )
      );
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Nonnull
  @Override
  public List<AuthUserEntity> findAll() {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(url));
    return jdbcTemplate.query(
        """
               SELECT * FROM "user"
            """,
        AuthUserEntityRowMapper.instance
    );
  }
}
