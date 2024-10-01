package guru.qa.niffler.data.dao.implementation;

import guru.qa.niffler.data.dao.auth.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.model.Authority;

import java.sql.*;
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
    public AuthAuthorityEntity create(UUID userId, String authority) {
        AuthAuthorityEntity entity = new AuthAuthorityEntity();
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO authority (user_id, authority) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setObject(1, userId);
            ps.setObject(2, authority);
            ps.executeUpdate();
            UUID generateKey;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generateKey = rs.getObject("id", UUID.class);
                } else throw new SQLException("Can't find ID in ResultSet");
            }
            entity.setId(generateKey);

            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<List<AuthAuthorityEntity>> findById(UUID id) {
        List<AuthAuthorityEntity> result = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM authority WHERE  id = ?"
        )) {
            ps.setObject(1, id);
            ps.execute();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AuthAuthorityEntity entity = new AuthAuthorityEntity();
                    entity.setId(rs.getObject("id", UUID.class));
                    entity.setUserId(rs.getObject("user_id", UUID.class));
                    entity.setAuthority(rs.getObject("authority", Authority.class));
                    result.add(entity);
                }
                if (!result.isEmpty()) {
                    return Optional.of(result);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<List<AuthAuthorityEntity>> findByUserId(UUID userId) {
        List<AuthAuthorityEntity> result = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM authority WHERE  user_id = ?"
        )) {
            ps.setObject(1, userId);
            ps.execute();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AuthAuthorityEntity entity = new AuthAuthorityEntity();
                    entity.setId(rs.getObject("id", UUID.class));
                    entity.setUserId(rs.getObject("user_id", UUID.class));
                    entity.setAuthority(rs.getObject("authority", Authority.class));
                    result.add(entity);
                }
                if (!result.isEmpty()) {
                    return Optional.of(result);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(UUID userId, String authority) {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM authority WHERE user_id = ?;" +
                        "INSERT INTO authority (user_id, authority) VALUES (?, ?);"
        )) {
            ps.setObject(1, userId);
            ps.setObject(2, userId);
            ps.setString(3, authority);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(AuthAuthorityEntity user) {
        UUID categoryId = user.getId();
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM authority WHERE id = ?"
        )) {
            ps.setObject(1, categoryId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
