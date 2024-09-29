package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.auth.UserEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.UUID;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {
    private final Connection connection;

    public AuthAuthorityDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public AuthorityEntity create(AuthorityEntity authority) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO \"authority\" (user_id, authority) " +
                        "VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            preparedStatement.setObject(1, authority.getUser().getId());
            preparedStatement.setString(1, authority.getAuthority().name());

            preparedStatement.executeUpdate();

            final UUID generatedKey;

            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can't find id from ResultSet");
                }
            }
            authority.setId(generatedKey);
            return authority;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthorityEntity> findById(UUID id) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM \"authority\" WHERE id = ?"
        )) {
            ps.setObject(1, id);

            ps.executeUpdate();

            try (ResultSet resultSet = ps.getResultSet()) {
                if (resultSet.next()) {
                    AuthorityEntity authority = new AuthorityEntity();
                    authority.setId(id);
                    authority.setAuthority(resultSet.getObject("authority", Authority.class));
                    authority.setUser(resultSet.getObject("user_id", UserEntity.class));
                    return Optional.of(authority);
                } else {
                    return Optional.empty();
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(AuthorityEntity authority) {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM  \"authority\" where id = ?"
        )) {
            ps.setObject(1, authority.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
