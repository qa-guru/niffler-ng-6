package guru.qa.niffler.data.dao.impl.jdbc;

import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.*;

@Slf4j
public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {

    private final Connection connection;

    public AuthAuthorityDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(AuthAuthorityEntity... authorityEntities) {

        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO \"authority\" (user_id, authority) VALUES (?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            for (AuthAuthorityEntity authorityEntity : authorityEntities) {
                ps.setObject(1, authorityEntity.getUserId());
                ps.setString(2, authorityEntity.getAuthority().name());
                ps.addBatch();
                ps.clearParameters();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Optional<AuthAuthorityEntity> findById(UUID id) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM \"authority\" WHERE id = ?"
        )) {
            ps.setObject(1, id);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    AuthAuthorityEntity authorityEntity = new AuthAuthorityEntity();
                    authorityEntity.setId(rs.getObject("id", UUID.class));
                    authorityEntity.setUserId(rs.getObject("user_id", UUID.class));
                    authorityEntity.setAuthority(Authority.valueOf(rs.getString("authority")));
                    return Optional.of(authorityEntity);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AuthAuthorityEntity> findByUserId(UUID userId) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM \"authority\" WHERE user_id = ?"
        )) {
            ps.setObject(1, userId);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                List<AuthAuthorityEntity> authorities = new ArrayList<>();
                while (rs.next() && rs.getObject("id", UUID.class) != null) {
                    AuthAuthorityEntity authorityEntity = new AuthAuthorityEntity();
                    authorityEntity.setId(rs.getObject("id", UUID.class));
                    authorityEntity.setUserId(rs.getObject("user_id", UUID.class));
                    authorityEntity.setAuthority(Authority.valueOf(rs.getString("authority")));
                    authorities.add(authorityEntity);
                }
                return authorities;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(AuthAuthorityEntity... authorityEntities) {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM \"authority\" WHERE id = ?"
        )) {
            for (AuthAuthorityEntity authorityEntity : authorityEntities) {
                ps.setObject(1, authorityEntity.getId());
                ps.addBatch();
                ps.clearParameters();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}