package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.entity.auth.UserEntity;
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
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    private final Connection connection;

    public AuthUserDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public UserEntity create(UserEntity user) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO \"user\" " +
                        "(username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, user.getUsername());
            ps.setString(2, pe.encode(user.getPassword()));
            ps.setBoolean(3, user.getEnabled());
            ps.setBoolean(4, user.getAccountNonExpired());
            ps.setBoolean(5, user.getAccountNonExpired());
            ps.setBoolean(6, user.getCredentialsNonExpired());

            ps.executeUpdate();

            final java.util.UUID generatedKey;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", java.util.UUID.class);
                } else {
                    throw new SQLException("Can't find id from ResultSet");
                }
                user.setId(generatedKey);
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM \"user\" WHERE id = ?"
        )) {
            ps.setObject(1, id);

            ps.executeUpdate();

            try (ResultSet resultSet = ps.getResultSet()) {
                if (resultSet.next()) {
                    UserEntity user = getUserEntity(resultSet);
                    return Optional.of(user);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM  \"user\" WHERE username = ?"
        )) {
            ps.setString(1, username);

            ps.executeUpdate();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    UserEntity user = getUserEntity(rs);
                    return Optional.of(user);
                } else {
                    return Optional.empty();
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(UUID userId) {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM  \"user\" WHERE id = ?"
        )) {
            ps.setObject(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private UserEntity getUserEntity(ResultSet resultSet) throws SQLException {
        UserEntity user = new UserEntity();
        user.setId(resultSet.getObject("id", UUID.class));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        user.setEnabled(resultSet.getBoolean("enabled"));
        user.setAccountNonLocked(resultSet.getBoolean("account_non_locked"));
        user.setAccountNonExpired(resultSet.getBoolean("account_non_expired"));
        user.setCredentialsNonExpired(resultSet.getBoolean("credentials_non_expired"));
        return user;
    }
}
