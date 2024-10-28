package guru.qa.niffler.data.dao.imp;

import guru.qa.niffler.data.dao.UdUserDao;
import guru.qa.niffler.data.entity.userdata.UdUserEntity;
import guru.qa.niffler.enums.CurrencyValuesEnum;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UdUserDaoJdbc implements UdUserDao {
  private final Connection connection;

  public UdUserDaoJdbc(Connection connection) {
    this.connection = connection;
  }

  @Override
  public UdUserEntity create(UdUserEntity user) {
    try (PreparedStatement ps = connection.prepareStatement(
        "INSERT INTO \"user\" (username, currency, firstname, surname, photo, photo_small, full_name) " +
            "VALUES(?, ?, ?, ?, ?, ?, ?)",
        Statement.RETURN_GENERATED_KEYS
    )) {
      ps.setString(1, user.getUsername());
      ps.setString(2, user.getCurrency().name());
      ps.setString(3, user.getFirstname());
      ps.setString(4, user.getSurname());
      ps.setBytes(5, user.getPhoto());
      ps.setBytes(6, user.getPhotoSmall());
      ps.setString(7, user.getFullname());

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
  public Optional<UdUserEntity> findById(UUID id) {
    try (PreparedStatement ps = connection.prepareStatement(
        "SELECT * FROM \"user\" WHERE id = ?"
    )) {
      ps.setObject(1, id);
      ps.execute();

      Optional<UdUserEntity> lue = Optional.of(new UdUserEntity());
      try (ResultSet rs = ps.getResultSet()) {
        UdUserEntity ue = new UdUserEntity();
        ue.setId(rs.getObject("id", UUID.class));
        ue.setUsername(rs.getString("username"));
        ue.setCurrency(CurrencyValuesEnum.valueOf(rs.getString("currency")));
        ue.setFirstname(rs.getString("firstname"));
        ue.setSurname(rs.getString("surname"));
        ue.setPhoto(rs.getBytes("photo"));
        ue.setPhotoSmall(rs.getBytes("photo_small"));
        ue.setFullname(rs.getString("full_name"));
        return lue;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<UdUserEntity> findAll() {
    try (PreparedStatement ps = connection.prepareStatement(
        "SELECT * FROM \"user\""
    )) {
      ps.execute();

      List<UdUserEntity> lse = new ArrayList<>();
      try (ResultSet rs = ps.getResultSet()) {
        while (rs.next()) {
          UdUserEntity ue = new UdUserEntity();
          ue.setId(rs.getObject("id", UUID.class));
          ue.setUsername(rs.getString("username"));
          ue.setCurrency(CurrencyValuesEnum.valueOf(rs.getString("currency")));
          ue.setFirstname(rs.getString("firstname"));
          ue.setSurname(rs.getString("surname"));
          ue.setPhoto(rs.getBytes("photo"));
          ue.setPhotoSmall(rs.getBytes("photo_small"));
          ue.setFullname(rs.getString("full_name"));

          lse.add(ue);
        }
        return lse;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
