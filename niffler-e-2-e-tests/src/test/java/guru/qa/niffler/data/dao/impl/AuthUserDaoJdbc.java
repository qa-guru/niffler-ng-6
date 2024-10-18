package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class AuthUserDaoJdbc implements AuthUserDao {

  private static final Config CFG = Config.getInstance();

  @Override
  public AuthUserEntity create(AuthUserEntity user) {
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
        "INSERT INTO \"user\" (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
            "VALUES (?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
      ps.setString(1, user.getUsername());
      ps.setString(2, user.getPassword());
      ps.setBoolean(3, user.getEnabled());
      ps.setBoolean(4, user.getAccountNonExpired());
      ps.setBoolean(5, user.getAccountNonLocked());
      ps.setBoolean(6, user.getCredentialsNonExpired());

      ps.executeUpdate();

      final UUID generatedKey;
      try (ResultSet rs = ps.getGeneratedKeys()) {
        if (rs.next()) {
          generatedKey = rs.getObject("id", UUID.class);
        } else {
          throw new SQLException("Can`t find id in ResultSet");
        }
      }
      user.setId(generatedKey);
      return user;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<AuthUserEntity> findById(UUID id) {
    try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement("SELECT * FROM \"user\" WHERE id = ?")) {
      ps.setObject(1, id);

      ps.execute();

      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {
          AuthUserEntity result = new AuthUserEntity();
          result.setId(rs.getObject("id", UUID.class));
          result.setUsername(rs.getString("username"));
          result.setPassword(rs.getString("password"));
          result.setEnabled(rs.getBoolean("enabled"));
          result.setAccountNonExpired(rs.getBoolean("account_non_expired"));
          result.setAccountNonLocked(rs.getBoolean("account_non_locked"));
          result.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
          return Optional.of(result);
        } else {
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
