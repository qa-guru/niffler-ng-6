package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.UUID;

public class AuthUserDaoJdbc implements AuthUserDao {

    private final Connection connection;

    public AuthUserDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public AuthUserEntity create(AuthUserEntity userAuth) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO public.user( username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)" +
                        " VALUES (?, ?, ?, ?, ?, ?);",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, userAuth.getUsername());
            ps.setString(2, userAuth.getPassword());
            ps.setBoolean(3, userAuth.isEnabled());
            ps.setBoolean(4, userAuth.isAccountNonExpired());
            ps.setBoolean(5, userAuth.isAccountNonLocked());
            ps.setBoolean(6, userAuth.isCredentialsNonExpired());

            ps.executeUpdate();
            final UUID generationKey;

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generationKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can't find id in ResultSet");
                }
            }
            userAuth.setId(generationKey);
            return userAuth;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
