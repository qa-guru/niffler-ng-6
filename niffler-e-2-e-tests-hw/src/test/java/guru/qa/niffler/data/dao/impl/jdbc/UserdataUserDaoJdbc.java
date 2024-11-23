package guru.qa.niffler.data.dao.impl.jdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.rest.CurrencyValues;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static guru.qa.niffler.data.tpl.Connections.holder;

@ParametersAreNonnullByDefault
public class UserdataUserDaoJdbc implements UserdataUserDao {

    private static final String USERDATA_JDBC_URL = Config.getInstance().userdataJdbcUrl();

    public @Nonnull UserEntity create(UserEntity user) {

        try (PreparedStatement ps = holder(USERDATA_JDBC_URL).connection().prepareStatement(
                "INSERT INTO \"user\" (username, currency, firstname, surname, photo, photo_small, full_name) " +
                        "VALUES ( ?, ?, ?, ?, ?, ?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS
        )) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getCurrency().name());
            ps.setString(3, user.getFirstName());
            ps.setString(4, user.getSurname());
            ps.setBytes(5, user.getPhoto());
            ps.setBytes(6, user.getPhotoSmall());
            ps.setString(7, user.getFullName());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return fromResultSet(rs);
                } else {
                    throw new SQLException("Could not find 'id' in ResultSet");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public @Nonnull Optional<UserEntity> findById(UUID id) {

        try (PreparedStatement ps = holder(USERDATA_JDBC_URL).connection().prepareStatement(
                "SELECT * FROM \"user\" WHERE id = ?"
        )) {

            ps.setObject(1, id);
            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                return rs.next()
                        ? Optional.of(fromResultSet(rs))
                        : Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public @Nonnull Optional<UserEntity> findByUsername(String username) {

        try (PreparedStatement ps = holder(USERDATA_JDBC_URL).connection().prepareStatement(
                "SELECT * FROM \"user\" WHERE username = ?"
        )) {

            ps.setString(1, username);
            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                return rs.next()
                        ? Optional.of(fromResultSet(rs))
                        : Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public @Nonnull List<UserEntity> findAll() {

        try (PreparedStatement ps = holder(USERDATA_JDBC_URL).connection().prepareStatement(
                "SELECT * FROM \"user\""
        )) {

            ps.execute();

            List<UserEntity> users = new ArrayList<>();
            try (ResultSet rs = ps.getResultSet()) {
                while (rs.next()) {
                    users.add(fromResultSet(rs));
                }
            }
            return users;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public @Nonnull UserEntity update(UserEntity user) {

        try (PreparedStatement ps = holder(USERDATA_JDBC_URL).connection().prepareStatement(
                "UPDATE \"user\" SET username = ?, currency = ?, firstname = ?, surname = ?, photo = ?, photo_small = ?, full_name = ? WHERE id = ?"
        )) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getCurrency().name());
            ps.setString(3, user.getFirstName());
            ps.setString(4, user.getSurname());
            ps.setBytes(5, user.getPhoto());
            ps.setBytes(6, user.getPhotoSmall());
            ps.setString(7, user.getFullName());
            ps.setObject(8, user.getId());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return fromResultSet(rs);
                } else {
                    throw new SQLException("Could not find 'id' in ResultSet");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<UserEntity> getIncomeInvitations(UserEntity user) {
        String sql = """
                SELECT
                    u.id
                    , u.username
                    , u.currency
                    , u.firstname
                    , u.surname
                    , u.full_name
                FROM
                    "friendship" fr
                INNER JOIN
                    "user" u
                ON
                    fr.requester_id = u.id
                WHERE
                    fr.addressee_id = ?
                AND
                    fr.status = ?""";
        try (PreparedStatement ps = holder(USERDATA_JDBC_URL).connection().prepareStatement(sql)) {

            ps.setObject(1, user.getId());
            ps.setObject(2, FriendshipStatus.PENDING);
            ps.execute();

            List<UserEntity> users = new ArrayList<>();
            try (ResultSet rs = ps.getResultSet()) {
                while (rs.next()) {
                    users.add(fromResultSet(rs));
                }
            }
            return users;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UserEntity> getOutcomeInvitations(UserEntity user) {
        String sql = """
                SELECT
                    u.id
                    , u.username
                    , u.currency
                    , u.firstname
                    , u.surname
                    , u.full_name
                FROM
                    "friendship" fr
                INNER JOIN
                    "user" u
                ON
                    fr.addressee_id = u.id
                WHERE
                    fr.requester_id = ?
                AND
                    fr.status = ?""";
        try (PreparedStatement ps = holder(USERDATA_JDBC_URL).connection().prepareStatement(sql)) {

            ps.setObject(1, user.getId());
            ps.setObject(2, FriendshipStatus.PENDING);
            ps.execute();

            List<UserEntity> users = new ArrayList<>();
            try (ResultSet rs = ps.getResultSet()) {
                while (rs.next()) {
                    users.add(fromResultSet(rs));
                }
            }
            return users;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UserEntity> getFriends(UserEntity user) {

        String sql = """
                SELECT
                    u.id
                    , u.username
                    , u.currency
                    , u.firstname
                    , u.surname
                    , u.full_name
                FROM
                    "friendship" fr
                INNER JOIN
                    "user" u
                ON
                    fr.addressee_id = u.id
                WHERE
                    fr.requester_id = ?
                AND
                    fr.status = ?""";
        try (PreparedStatement ps = holder(USERDATA_JDBC_URL).connection().prepareStatement(sql)) {

            ps.setObject(1, user.getId());
            ps.setObject(2, FriendshipStatus.ACCEPTED);
            ps.execute();

            List<UserEntity> users = new ArrayList<>();
            try (ResultSet rs = ps.getResultSet()) {
                while (rs.next()) {
                    users.add(fromResultSet(rs));
                }
            }
            return users;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendInvitation(UserEntity requester, UserEntity addressee) {
        try (PreparedStatement ps = holder(USERDATA_JDBC_URL).connection().prepareStatement(
                "INSERT INTO friendship (requester_id, addressee_id, status, created_date)  VALUES(?, ?, ?, ?)"
        )) {
            ps.setObject(1, requester.getId());
            ps.setObject(2, addressee.getId());
            ps.setString(3, FriendshipStatus.PENDING.name());
            ps.setDate(4, new java.sql.Date(new Date().getTime()));
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeInvitation(UserEntity requester, UserEntity addressee) {
        try (PreparedStatement ps = holder(USERDATA_JDBC_URL).connection().prepareStatement(
                "DELETE FROM friendship WHERE requester_id = ? AND addressee_id = ? AND status = ?"
        )) {
            ps.setObject(1, requester.getId());
            ps.setObject(2, addressee.getId());
            ps.setString(3, FriendshipStatus.PENDING.name());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addFriend(UserEntity requester, UserEntity addressee) {
        try (PreparedStatement ps = holder(USERDATA_JDBC_URL).connection().prepareStatement(
                "INSERT INTO friendship (requester_id, addressee_id, status, created_date)  VALUES(?, ?, ?, ?)"
        )) {

            var sqlDate = new java.sql.Date(new Date().getTime());

            ps.setObject(1, requester.getId());
            ps.setObject(2, addressee.getId());
            ps.setString(3, FriendshipStatus.ACCEPTED.name());
            ps.setDate(4, sqlDate);

            ps.addBatch();
            ps.clearParameters();

            ps.setObject(1, requester.getId());
            ps.setObject(2, addressee.getId());
            ps.setString(3, FriendshipStatus.ACCEPTED.name());
            ps.setDate(4, sqlDate);

            ps.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeFriend(UserEntity requester, UserEntity addressee) {
        try (PreparedStatement ps = holder(USERDATA_JDBC_URL).connection().prepareStatement(
                "DELETE FROM friendship WHERE requester_id = ? AND addressee_id = ? AND status = ?"
        )) {

            ps.setObject(1, requester.getId());
            ps.setObject(2, addressee.getId());
            ps.setString(3, FriendshipStatus.ACCEPTED.name());

            ps.addBatch();
            ps.clearParameters();

            ps.setObject(1, requester.getId());
            ps.setObject(2, addressee.getId());
            ps.setString(3, FriendshipStatus.ACCEPTED.name());

            ps.executeBatch();

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

    @Override
    public void removeAll() {

        try (PreparedStatement ps = holder(USERDATA_JDBC_URL).connection().prepareStatement(
                "TRUNCATE TABLE \"user\" CASCADE"
        )) {
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private @Nonnull UserEntity fromResultSet(ResultSet rs) throws SQLException {
        return UserEntity.builder()
                .id(rs.getObject("id", UUID.class))
                .username(rs.getString("username"))
                .currency(CurrencyValues.valueOf(rs.getString("currency")))
                .firstName(rs.getString("firstname"))
                .surname(rs.getString("surname"))
                .photo(rs.getBytes("photo"))
                .photoSmall(rs.getBytes("photo_small"))
                .fullName(rs.getString("full_name"))
                .build();
    }

    private @Nonnull UserEntity fromResultSetFriend(ResultSet rs) throws SQLException {
        return UserEntity.builder()
                .id(rs.getObject("id", UUID.class))
                .username(rs.getString("username"))
                .firstName(rs.getString("firstname"))
                .surname(rs.getString("surname"))
                .fullName(rs.getString("full_name"))
                .build();
    }

}
