package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.rowMapper.UserdataUserRowMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class UserdataUserRepositoryJdbc implements UserdataUserRepository {

    private static final String USERDATA_JDBC_URL = Config.getInstance().userdataJdbcUrl();

    @Override
    public UserEntity create(UserEntity user) {

        try (PreparedStatement userdataPs = holder(USERDATA_JDBC_URL).connection().prepareStatement(
                "INSERT INTO \"user\" (username, currency, firstname, surname, photo, photo_small, full_name) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            userdataPs.setString(1, user.getUsername());
            userdataPs.setString(2, user.getCurrency().name());
            userdataPs.setString(3, user.getFirstName());
            userdataPs.setString(4, user.getSurname());
            userdataPs.setBytes(5, user.getPhoto());
            userdataPs.setBytes(6, user.getPhotoSmall());
            userdataPs.setString(7, user.getFullName());
            userdataPs.executeUpdate();

            final UUID generatedKey;
            try (ResultSet rs = userdataPs.getGeneratedKeys()) {
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
    public Optional<UserEntity> findById(UUID id) {

        try (PreparedStatement ps = holder(USERDATA_JDBC_URL).connection().prepareStatement(
                "SELECT * FROM \"user\" where id = ?"
        )) {

            ps.setObject(1, id);
            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                return rs.next()
                        ? Optional.ofNullable(UserdataUserRowMapper.INSTANCE.mapRow(rs, 1))
                        : Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        try (PreparedStatement ps = holder(USERDATA_JDBC_URL).connection().prepareStatement(
                "SELECT * FROM \"user\" where username = ?"
        )) {

            ps.setString(1, username);
            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                return rs.next()
                        ? Optional.ofNullable(UserdataUserRowMapper.INSTANCE.mapRow(rs, 1))
                        : Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UserEntity> findAll() {

        try (PreparedStatement ps = holder(USERDATA_JDBC_URL).connection().prepareStatement(
                "SELECT * FROM \"user\""
        )) {

            ps.execute();

            List<UserEntity> users = new ArrayList<>();
            try (ResultSet rs = ps.getResultSet()) {
                while (rs.next()) {
                        users.add(UserdataUserRowMapper.INSTANCE.mapRow(rs, 1));
                }
                return users;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void sendInvitation(UserEntity requester, UserEntity addressee, FriendshipStatus status) {
        try (PreparedStatement frPs = holder(USERDATA_JDBC_URL).connection().prepareStatement(
                "INSERT INTO \"friendship\" (requester_id, addressee_id, status, created_date) " +
                        "VALUES ( ?, ?, ?, ?)")
        ) {
            frPs.setObject(1, requester.getId());
            frPs.setObject(2, addressee.getId());
            frPs.setString(3, status.name());
            frPs.setDate(4, new java.sql.Date(new Date().getTime()));

            frPs.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addFriend(UserEntity requester, UserEntity addressee) {
        try (PreparedStatement frPs = holder(USERDATA_JDBC_URL).connection().prepareStatement(
                "INSERT INTO \"friendship\" (requester_id, addressee_id, status, created_date) " +
                        "VALUES ( ?, ?, ?, ?)")
        ) {
            frPs.setObject(1, requester.getId());
            frPs.setObject(2, addressee.getId());
            frPs.setString(3, FriendshipStatus.ACCEPTED.name());
            frPs.setDate(4, new java.sql.Date(new Date().getTime()));
            frPs.addBatch();
            frPs.clearParameters();

            frPs.setObject(1, addressee.getId());
            frPs.setObject(2, requester.getId());
            frPs.setString(3, FriendshipStatus.ACCEPTED.name());
            frPs.setDate(4, new java.sql.Date(new Date().getTime()));
            frPs.addBatch();

            frPs.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(UserEntity user) {

        try (PreparedStatement ps = holder(USERDATA_JDBC_URL).connection().prepareStatement(
                "DELETE FROM \"user\" WHERE id = ?"
        )) {
            ps.setObject(1, user.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


}
