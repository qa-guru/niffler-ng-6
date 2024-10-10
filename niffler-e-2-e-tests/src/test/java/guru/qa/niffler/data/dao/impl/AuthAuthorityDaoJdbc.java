package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.mapper.AuthorityEntityRowMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static guru.qa.niffler.data.jdbc.Connections.holder;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {

  private static final Config CFG = Config.getInstance();
  private final String url = CFG.authJdbcUrl();

  @Override
  public void create(AuthorityEntity... authority) {
    try (PreparedStatement ps = holder(url).connection().prepareStatement(
        "INSERT INTO authority (user_id, authority) VALUES (?, ?)")) {
      for (AuthorityEntity a : authority) {
        ps.setObject(1, a.getUser().getId());
        ps.setString(2, a.getAuthority().name());
        ps.addBatch();
        ps.clearParameters();
      }
      ps.executeBatch();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<AuthorityEntity> findAll() {
    try (PreparedStatement ps = holder(url).connection().prepareStatement(
        "SELECT * FROM authority")) {
      ps.execute();
      List<AuthorityEntity> result = new ArrayList<>();
      try (ResultSet rs = ps.getResultSet()) {
        while (rs.next()) {
          result.add(AuthorityEntityRowMapper.instance.mapRow(rs, rs.getRow()));
        }
      }
      return result;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<AuthorityEntity> findAllByUserId(UUID userId) {
    try (PreparedStatement ps = holder(url).connection().prepareStatement(
        "SELECT * FROM authority where user_id = ?")) {
      ps.setObject(1, userId);
      ps.execute();
      List<AuthorityEntity> result = new ArrayList<>();
      try (ResultSet rs = ps.getResultSet()) {
        while (rs.next()) {
          result.add(AuthorityEntityRowMapper.instance.mapRow(rs, rs.getRow()));
        }
      }
      return result;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
