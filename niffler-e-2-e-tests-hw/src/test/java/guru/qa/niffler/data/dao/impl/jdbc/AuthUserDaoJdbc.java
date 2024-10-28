package guru.qa.niffler.data.dao.impl.jdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import lombok.NonNull;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class AuthUserDaoJdbc implements AuthUserDao {

    private static final String AUTH_JDBC_URL = Config.getInstance().authJdbcUrl();

    private static final PasswordEncoder PASSWORD_ENCODER = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public AuthUserEntity create(@NonNull AuthUserEntity user) {

        try (PreparedStatement ps = holder(AUTH_JDBC_URL).connection().prepareStatement(
                "INSERT INTO \"user\" (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
                        "VALUES ( ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, user.getUsername());
            ps.setString(2, PASSWORD_ENCODER.encode(user.getPassword()));
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
                    throw new SQLException("Can`t find 'id' in ResultSet");
                }
            }
            user.setId(generatedKey);
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthUserEntity> findById(@NonNull UUID id) {
        try (PreparedStatement ps = holder(AUTH_JDBC_URL).connection().prepareStatement(
                "SELECT * FROM \"user\" WHERE id = ?"
        )) {
            ps.setObject(1, id);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    AuthUserEntity authUserEntity = new AuthUserEntity();
                    authUserEntity.setId(rs.getObject("id", UUID.class));
                    authUserEntity.setUsername(rs.getString("username"));
                    authUserEntity.setPassword(rs.getString("password")); // here password is encoded
                    authUserEntity.setEnabled(rs.getBoolean("enabled"));
                    authUserEntity.setAccountNonExpired(rs.getBoolean("account_non_expired"));
                    authUserEntity.setAccountNonLocked(rs.getBoolean("account_non_locked"));
                    authUserEntity.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
                    return Optional.of(authUserEntity);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthUserEntity> findByUsername(@NonNull String username) {
        try (PreparedStatement ps = holder(AUTH_JDBC_URL).connection().prepareStatement(
                "SELECT * FROM \"user\" WHERE username = ?"
        )) {
            ps.setObject(1, username);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    AuthUserEntity authUserEntity = new AuthUserEntity();
                    authUserEntity.setId(rs.getObject("id", UUID.class));
                    authUserEntity.setUsername(rs.getString("username"));
                    authUserEntity.setPassword(rs.getString("password")); // here password is encoded
                    authUserEntity.setEnabled(rs.getBoolean("enabled"));
                    authUserEntity.setAccountNonExpired(rs.getBoolean("account_non_expired"));
                    authUserEntity.setAccountNonLocked(rs.getBoolean("account_non_locked"));
                    authUserEntity.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
                    return Optional.of(authUserEntity);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AuthUserEntity> findAll() {
        try (PreparedStatement ps = holder(AUTH_JDBC_URL).connection().prepareStatement(
                "SELECT * FROM \"user\""
        )) {
            ps.execute();
            List<AuthUserEntity> users = new ArrayList<>();
            try (ResultSet rs = ps.getResultSet()) {
                while (rs.next()) {
                    AuthUserEntity authUserEntity = new AuthUserEntity();
                    authUserEntity.setId(rs.getObject("id", UUID.class));
                    authUserEntity.setUsername(rs.getString("username"));
                    authUserEntity.setPassword(rs.getString("password")); // here password is encoded
                    authUserEntity.setEnabled(rs.getBoolean("enabled"));
                    authUserEntity.setAccountNonExpired(rs.getBoolean("account_non_expired"));
                    authUserEntity.setAccountNonLocked(rs.getBoolean("account_non_locked"));
                    authUserEntity.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
                    users.add(authUserEntity);
                }

                return users;

            }
        } catch (
                SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AuthUserEntity update(@NonNull AuthUserEntity user) {
        try (
                PreparedStatement userPs = holder(AUTH_JDBC_URL).connection().prepareStatement(
                        "UPDATE \"user\" SET username = ?, password = ?, enabled = ?, account_non_expired = ?, account_non_locked = ?, credentials_non_expired = ? WHERE id = ?");

                PreparedStatement authorityPs = holder(AUTH_JDBC_URL).connection().prepareStatement(
                        "UPDATE \"authority\" SET user_id = ?, authority = ? WHERE id = ?")
        ) {

            userPs.setString(1, user.getUsername());
            userPs.setString(2, user.getPassword());
            userPs.setBoolean(3, user.getEnabled());
            userPs.setBoolean(4, user.getAccountNonExpired());
            userPs.setBoolean(5, user.getAccountNonLocked());
            userPs.setBoolean(6, user.getCredentialsNonExpired());
            userPs.setObject(7, user.getId());

            userPs.executeUpdate();

            final UUID generatedKey;
            try (ResultSet rs = userPs.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can`t find id in ResultSet");
                }
            }
            user.setId(generatedKey);

            for (AuthAuthorityEntity a : user.getAuthorities()) {
                authorityPs.setObject(1, a.getUser().getId());
                authorityPs.setString(2, a.getAuthority().name());
                authorityPs.setObject(3, a.getId());
                authorityPs.addBatch();
                authorityPs.clearParameters();
            }
            authorityPs.executeBatch();
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(@NonNull AuthUserEntity userEntity) {
        try (PreparedStatement ps = holder(AUTH_JDBC_URL).connection().prepareStatement(
                "DELETE FROM \"user\" WHERE id = ?"
        )) {
            ps.setObject(1, userEntity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}