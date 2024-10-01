package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;

import java.sql.*;
import java.util.UUID;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {

    Connection connection;

    public AuthAuthorityDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public AuthAuthorityEntity create(AuthAuthorityEntity authAuthority) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO public.authority(user_id, authority) " +
                        "VALUES (?, ?);",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setObject(1, authAuthority.getUserId());
            ps.setString(2, authAuthority.getAuthority().toString());

            ps.executeUpdate();
            final UUID generationKey;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generationKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can't find id in ResultSet");
                }
            }
            authAuthority.setId(generationKey);
            return authAuthority;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
