package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthUserDaoJdbc {
    private final Connection connection;

    public AuthUserDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    public AuthUserEntity create(AuthUserEntity authUser) {
        String insertUserSQL = "INSERT INTO \"user\" (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
                "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";

        try (PreparedStatement userPs = connection.prepareStatement(insertUserSQL)) {
            userPs.setString(1, authUser.getUsername());
            userPs.setString(2, authUser.getPassword());
            userPs.setBoolean(3, authUser.getEnabled());
            userPs.setBoolean(4, authUser.getAccountNonExpired());
            userPs.setBoolean(5, authUser.getAccountNonLocked());
            userPs.setBoolean(6, authUser.getCredentialsNonExpired());

            UUID userId = null;
            try (ResultSet rs = userPs.executeQuery()) {
                if (rs.next()) {
                    userId = rs.getObject("id", UUID.class);
                }
            }

            authUser.setId(userId);
            return authUser;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public AuthUserEntity findByUsername(String username) {
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM \"user\" WHERE username = ?")) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRowToAuthUserEntity(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private AuthUserEntity mapRowToAuthUserEntity(ResultSet rs) throws SQLException {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setId(rs.getObject("id", UUID.class));
        authUser.setUsername(rs.getString("username"));
        authUser.setPassword(rs.getString("password"));
        authUser.setEnabled(rs.getBoolean("enabled"));
        authUser.setAccountNonExpired(rs.getBoolean("account_non_expired"));
        authUser.setAccountNonLocked(rs.getBoolean("account_non_locked"));
        authUser.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
        return authUser;
    }
}
