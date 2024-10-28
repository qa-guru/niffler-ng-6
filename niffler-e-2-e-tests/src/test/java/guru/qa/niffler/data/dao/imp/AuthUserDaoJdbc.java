package guru.qa.niffler.data.dao.imp;

import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.UUID;

public class AuthUserDaoJdbc implements AuthUserDao {
  private final Connection connection;
  private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  public AuthUserDaoJdbc(Connection connection) {
    this.connection = connection;
  }

  @Override
  public AuthUserEntity create(AuthUserEntity user) {
    try (PreparedStatement ps = connection.prepareStatement(
        "INSERT INTO public.user (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
            "VALUES(?, ?, ?, ?, ?, ?)",
        Statement.RETURN_GENERATED_KEYS
    )) {
      ps.setString(1, user.getUsername());
      ps.setString(2, pe.encode(user.getPassword()));
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
          throw new SQLException("Can't find id in ResultSet");
        }
      }
      user.setId(generatedKey);
      return user;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<AuthUserEntity> findUserByUsername(String username) {
    try (PreparedStatement ps = connection.prepareStatement(
        "SELECT * FROM public.user WHERE username = ?"
    )) {
      ps.setObject(1, username);
      ps.execute();
      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {
          AuthUserEntity ue = new AuthUserEntity();
          ue.setId(rs.getObject("id", UUID.class));
          ue.setUsername(rs.getString("username"));
          ue.setPassword(rs.getString("currency"));
          ue.setEnabled(rs.getBoolean("enabled"));
          ue.setAccountNonExpired(rs.getBoolean("account_non_expired"));
          ue.setAccountNonLocked(rs.getBoolean("account_non_locked"));
          ue.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));

          return Optional.of(ue);
        } else {
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteUserByUsername(String username) {
    try (PreparedStatement ps = connection.prepareStatement(
        "DELETE FROM public.user WHERE username = ?"
    )) {
      ps.setObject(1, username);
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
