package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.UserDao;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.UUID;

public class UserDaoJdbc implements UserDao {
    private static final Config CFG = Config.getInstance();
    private static final String USER_TABLE = "\"user\"";

    @Override
    public UserEntity createUser(UserEntity user) {
        String sql = String.format("INSERT INTO %s (username, firstname, surname, full_name, currency, photo, photo_small) VALUES (?, ?, ?, ?, ?, ?, ?)", USER_TABLE);

        try (Connection connection = Databases.connection(CFG.userdataJdbcUrl());
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            setUserParameters(ps, user);
            ps.executeUpdate();
            user.setId(getGeneratedKey(ps));
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        return findUserByQuery("SELECT * FROM " + USER_TABLE + " WHERE id = ?", ps -> ps.setObject(1, id));
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return findUserByQuery("SELECT * FROM " + USER_TABLE + " WHERE username = ?", ps -> ps.setString(1, username));
    }

    private Optional<UserEntity> findUserByQuery(String query, QueryParameterSetter setter) {
        try (Connection connection = Databases.connection(CFG.userdataJdbcUrl());
             PreparedStatement ps = connection.prepareStatement(query)) {

            setter.setParameters(ps);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUserEntity(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public void delete(UserEntity user) {
        deleteById(user.getId());
    }

    public void deleteById(UUID id) {
        String sql = String.format("DELETE FROM %s WHERE id = ?", USER_TABLE);
        try (Connection connection = Databases.connection(CFG.userdataJdbcUrl());
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setObject(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private UserEntity mapResultSetToUserEntity(ResultSet rs) throws SQLException {
        UserEntity user = new UserEntity();
        user.setId(rs.getObject("id", UUID.class));
        user.setUsername(rs.getString("username"));
        user.setFirstname(rs.getString("firstname"));
        user.setSurname(rs.getString("surname"));
        user.setFullname(rs.getString("full_name"));
        user.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
        user.setPhoto(rs.getBytes("photo"));
        user.setPhotoSmall(rs.getBytes("photo_small"));
        return user;
    }

    private UUID getGeneratedKey(PreparedStatement ps) throws SQLException {
        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return generatedKeys.getObject(1, UUID.class);
            } else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        }
    }

    private void setUserParameters(PreparedStatement ps, UserEntity user) throws SQLException {
        ps.setString(1, user.getUsername());
        ps.setString(2, user.getFirstname());
        ps.setString(3, user.getSurname());
        ps.setString(4, user.getFullname());
        ps.setString(5, user.getCurrency().name());
        ps.setBytes(6, user.getPhoto());
        ps.setBytes(7, user.getPhotoSmall());
    }

    @FunctionalInterface
    private interface QueryParameterSetter {
        void setParameters(PreparedStatement ps) throws SQLException;
    }
}
