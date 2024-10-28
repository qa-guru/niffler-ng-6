package guru.qa.niffler.data.dao.impl.jdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

@Slf4j
public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {

    private static final String AUTH_JDBC_URL = Config.getInstance().authJdbcUrl();

    @Override
    public void create(@NonNull AuthAuthorityEntity... authority) {

        try (PreparedStatement ps = holder(AUTH_JDBC_URL).connection().prepareStatement(
                "INSERT INTO \"authority\" (user_id, authority) VALUES (?, ?)")) {
            for (AuthAuthorityEntity authorityEntity : authority) {
                ps.setObject(1, authorityEntity.getUser().getId());
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
    public Optional<AuthAuthorityEntity> findById(@NonNull UUID id) {
        try (PreparedStatement ps = holder(AUTH_JDBC_URL).connection().prepareStatement(
                "SELECT * FROM \"authority\" WHERE id = ?"
        )) {
            ps.setObject(1, id);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    return Optional.of(fromResultSet(rs));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AuthAuthorityEntity> findByUserId(@NonNull UUID userId) {
        try (PreparedStatement ps = holder(AUTH_JDBC_URL).connection().prepareStatement(
                "SELECT * FROM \"authority\" WHERE user_id = ?"
        )) {
            ps.setObject(1, userId);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                List<AuthAuthorityEntity> authorities = new ArrayList<>();
                while (rs.next() && rs.getObject("id", UUID.class) != null) {
                    authorities.add(fromResultSet(rs));
                }
                return authorities;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AuthAuthorityEntity> findAll() {
        try (PreparedStatement ps = holder(AUTH_JDBC_URL).connection().prepareStatement(
                "SELECT * FROM \"authority\""
        )) {
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                List<AuthAuthorityEntity> authorities = new ArrayList<>();
                while (rs.next() && rs.getObject("id", UUID.class) != null) {
                    authorities.add(fromResultSet(rs));
                }
                return authorities;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(@NonNull AuthAuthorityEntity... authority) {

        try (PreparedStatement ps = holder(AUTH_JDBC_URL).connection().prepareStatement(
                "UPDATE \"authority\" SET user_id = ?, authority = ? WHERE id = ?")) {
            for (AuthAuthorityEntity authorityEntity : authority) {
                ps.setObject(1, authorityEntity.getUser().getId());
                ps.setString(2, authorityEntity.getAuthority().name());
                ps.setObject(3, authorityEntity.getId());
                ps.addBatch();
                ps.clearParameters();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void remove(@NonNull AuthAuthorityEntity... authority) {
        try (PreparedStatement ps = holder(AUTH_JDBC_URL).connection().prepareStatement(
                "DELETE FROM \"authority\" WHERE id = ?"
        )) {
            for (AuthAuthorityEntity authorityEntity : authority) {
                ps.setObject(1, authorityEntity.getId());
                ps.addBatch();
                ps.clearParameters();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private AuthAuthorityEntity fromResultSet(ResultSet rs) throws SQLException {
        return AuthAuthorityEntity.builder()
                .id(rs.getObject("id", UUID.class))
                .user(AuthUserEntity.builder().id(rs.getObject("user_id", UUID.class)).build())
                .authority(Authority.valueOf(rs.getString("authority")))
                .build();
    }

}