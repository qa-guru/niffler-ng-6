package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {
    private final Connection connection;

    public AuthAuthorityDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(AuthorityEntity... authority) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO \"authority\" (user_id, authority) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            for (AuthorityEntity authorityEntity : authority) {
                preparedStatement.setObject(1, authorityEntity.getUserId());
                preparedStatement.setString(2, authorityEntity.getAuthority().name());
                preparedStatement.addBatch();
                preparedStatement.clearParameters();
            }
            preparedStatement.executeBatch();
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
                    AuthorityEntity authority = getAuthorityEntity(resultSet);
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

    @Override
    public List<AuthorityEntity> findAll() {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM \"authority\""
        )) {
            ps.executeUpdate();
            List<AuthorityEntity> authorityEntities = new ArrayList<>();
            try (ResultSet resultSet = ps.getResultSet()) {
                if (resultSet.next()) {
                    AuthorityEntity authority = getAuthorityEntity(resultSet);
                    authorityEntities.add(authority);
                }
                return authorityEntities;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private AuthorityEntity getAuthorityEntity(ResultSet resultSet) throws SQLException {
        AuthorityEntity authority = new AuthorityEntity();
        authority.setId(resultSet.getObject("id", UUID.class));
        authority.setAuthority(resultSet.getObject("authority", Authority.class));
        authority.setUserId(resultSet.getObject("user_id", UUID.class));
        return authority;
    }
}
