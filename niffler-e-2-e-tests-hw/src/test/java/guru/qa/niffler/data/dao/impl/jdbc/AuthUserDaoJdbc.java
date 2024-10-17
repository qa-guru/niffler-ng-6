package guru.qa.niffler.data.dao.impl.jdbc;

import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthUserDaoJdbc implements AuthUserDao {

    private final Connection connection;

    public AuthUserDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    private static final PasswordEncoder PASSWORD_ENCODER = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public AuthUserEntity create(AuthUserEntity user) {

        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO \"user\" (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
                        "VALUES ( ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, user.getUsername());
            ps.setString(2, PASSWORD_ENCODER.encode(user.getPassword()));
            ps.setBoolean(3, user.isEnabled());
            ps.setBoolean(4, user.isAccountNonExpired());
            ps.setBoolean(5, user.isAccountNonLocked());
            ps.setBoolean(6, user.isCredentialsNonExpired());
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
    public Optional<AuthUserEntity> findById(UUID id) {
        try (PreparedStatement ps = connection.prepareStatement(
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
    public Optional<AuthUserEntity> findByUsername(String username) {
        try (PreparedStatement ps = connection.prepareStatement(
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
        try (PreparedStatement ps = connection.prepareStatement(
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
    public void delete(AuthUserEntity userEntity) {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM \"user\" WHERE id = ?"
        )) {
            ps.setObject(1, userEntity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}