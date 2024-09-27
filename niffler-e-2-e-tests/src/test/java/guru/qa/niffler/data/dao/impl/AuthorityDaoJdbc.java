package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class AuthorityDaoJdbc {
    private final Connection connection;

    public AuthorityDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    public void createAuthorities(List<AuthorityEntity> authorities, UUID userId) {
        String insertAuthoritySQL = "INSERT INTO authority (user_id, authority) VALUES (?, ?)";

        try (PreparedStatement authorityPs = connection.prepareStatement(insertAuthoritySQL)) {
            for (AuthorityEntity authority : authorities) {
                authorityPs.setObject(1, userId);
                authorityPs.setString(2, authority.getAuthority().name());
                authorityPs.addBatch();
            }
            authorityPs.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
