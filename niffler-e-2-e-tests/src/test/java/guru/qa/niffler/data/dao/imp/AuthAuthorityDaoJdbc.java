package guru.qa.niffler.data.dao.imp;

import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.enums.AuthorityEnum;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {
  private final Connection connection;

  public AuthAuthorityDaoJdbc(Connection connection) {
    this.connection = connection;
  }

  @Override
  public AuthAuthorityEntity create(AuthAuthorityEntity authority) {
    try (PreparedStatement ps = connection.prepareStatement(
        "INSERT INTO authority (user_id, authority) " +
            "VALUES(?, ?, ?, ?, ?, ?)",
        Statement.RETURN_GENERATED_KEYS
    )) {
      ps.setObject(1, authority.getUserId());
      ps.setObject(2, authority.getAuthority());

      ps.executeUpdate();

      final UUID generatedKey;
      try (ResultSet rs = ps.getGeneratedKeys()) {
        if (rs.next()) {
          generatedKey = rs.getObject("id", UUID.class);
        } else {
          throw new SQLException("Can't find id in ResultSet");
        }
      }
      authority.setId(generatedKey);
      return authority;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<AuthAuthorityEntity> findAuthoritiesByUserId(UUID userId) {
    try (PreparedStatement ps = connection.prepareStatement(
        "SELECT * FROM authority WHERE user_id = ?"
    )) {
      ps.setObject(1, userId);
      ps.execute();

      List<AuthAuthorityEntity> lae = new ArrayList<>();
      try (ResultSet rs = ps.getResultSet()) {
        while (rs.next()) {
          AuthAuthorityEntity ue = new AuthAuthorityEntity();
          ue.setId(rs.getObject("id", UUID.class));
          ue.setUserId(rs.getObject("user_id", UUID.class));
          ue.setAuthority(rs.getObject("authority", AuthorityEnum.class));

          lae.add(ue);
        }
        return lae;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteAuthorityById(UUID id) {
    try (PreparedStatement ps = connection.prepareStatement(
        "DELETE FROM authority WHERE id = ?"
    )) {
      ps.setObject(1, id);
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
