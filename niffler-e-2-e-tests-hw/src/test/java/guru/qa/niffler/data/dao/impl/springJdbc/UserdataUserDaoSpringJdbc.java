package guru.qa.niffler.data.dao.impl.springJdbc;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.rowMapper.UserdataUserFriendRowMapper;
import guru.qa.niffler.data.rowMapper.UserdataUserRowMapper;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class UserdataUserDaoSpringJdbc implements UserdataUserDao {

    private static final String USERDATA_JDBC_URL = Config.getInstance().userdataJdbcUrl();

    @Override
    public @Nonnull UserEntity create(UserEntity user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(USERDATA_JDBC_URL));
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            "INSERT INTO \"user\" (username, currency, firstname, surname, photo, photo_small, full_name) " +
                                    "VALUES (?, ?, ?, ?, ?, ?, ?)",
                            Statement.RETURN_GENERATED_KEYS
                    );
                    ps.setString(1, user.getUsername());
                    ps.setString(2, user.getCurrency().name());
                    ps.setString(3, user.getFirstName());
                    ps.setString(4, user.getSurname());
                    ps.setBytes(5, user.getPhoto());
                    ps.setBytes(6, user.getPhotoSmall());
                    ps.setString(7, user.getFullName());
                    return ps;
                },
                keyHolder
        );

        final UUID generatedKey = (UUID) keyHolder.getKeys().get("id");
        user.setId(generatedKey);
        return user;

    }

    @Override
    public @Nonnull Optional<UserEntity> findById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(USERDATA_JDBC_URL));
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "SELECT * FROM \"user\" WHERE id = ?",
                        UserdataUserRowMapper.INSTANCE,
                        id
                ));
    }

    @Override
    public @Nonnull Optional<UserEntity> findByUsername(String username) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(USERDATA_JDBC_URL));
        try {
            // QueryForObject not returns null if not found object. Method throws EmptyResultDataAccessException
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            "SELECT * FROM \"user\" WHERE username = ?",
                            UserdataUserRowMapper.INSTANCE,
                            username
                    ));
        } catch (
                EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public @Nonnull List<UserEntity> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(USERDATA_JDBC_URL));
        return jdbcTemplate.query(
                "SELECT * FROM \"user\"",
                UserdataUserRowMapper.INSTANCE
        );
    }

    @Override
    public @Nonnull UserEntity update(UserEntity user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(USERDATA_JDBC_URL));
        jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            "UPDATE \"user\" SET username = ?, currency = ?, firstname = ?, surname = ?, photo = ?, photo_small = ?, full_name = ? WHERE id = ?"
                    );
                    ps.setString(1, user.getUsername());
                    ps.setString(2, user.getCurrency().name());
                    ps.setString(3, user.getFirstName());
                    ps.setString(4, user.getSurname());
                    ps.setBytes(5, user.getPhoto());
                    ps.setBytes(6, user.getPhotoSmall());
                    ps.setString(7, user.getFullName());
                    return ps;
                }
        );
        return user;
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
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(USERDATA_JDBC_URL));
        return jdbcTemplate.query(
                sql,
                UserdataUserFriendRowMapper.INSTANCE,
                user.getId(),
                FriendshipStatus.PENDING.name()
        );
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
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(USERDATA_JDBC_URL));
        return jdbcTemplate.query(
                sql,
                UserdataUserFriendRowMapper.INSTANCE,
                user.getId(),
                FriendshipStatus.PENDING.name()
        );
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
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(USERDATA_JDBC_URL));
        return jdbcTemplate.query(
                sql,
                UserdataUserFriendRowMapper.INSTANCE,
                user.getId(),
                FriendshipStatus.ACCEPTED.name()
        );
    }

    @Override
    public void sendInvitation(UserEntity requester, UserEntity addressee) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(USERDATA_JDBC_URL));
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO friendship (requester_id, addressee_id, status, created_date) VALUES (?, ?, ?, ?)"
            );
            ps.setObject(1, requester.getId());
            ps.setObject(2, addressee.getId());
            ps.setString(3, FriendshipStatus.PENDING.name());
            ps.setDate(4, new java.sql.Date(new Date().getTime()));
            return ps;
        });
    }

    @Override
    public void removeInvitation(UserEntity requester, UserEntity addressee) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(USERDATA_JDBC_URL));
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "DELETE FROM friendship WHERE requester_id = ? AND addressee_id = ? AND status = ?"
            );
            ps.setObject(1, requester.getId());
            ps.setObject(2, addressee.getId());
            ps.setString(3, FriendshipStatus.PENDING.name());
            return ps;
        });
    }

    @Override
    public void addFriend(UserEntity requester, UserEntity addressee) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(USERDATA_JDBC_URL));
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO friendship (requester_id, addressee_id, status, created_date) VALUES (?, ?, ?, ?)"
            );

            var sqlDate = new java.sql.Date(new Date().getTime());

            ps.setObject(1, requester.getId());
            ps.setObject(2, addressee.getId());
            ps.setString(3, FriendshipStatus.ACCEPTED.name());
            ps.setDate(4, sqlDate);

            ps.addBatch();
            ps.clearParameters();

            ps.setObject(1, addressee.getId());
            ps.setObject(2, requester.getId());
            ps.setString(3, FriendshipStatus.ACCEPTED.name());
            ps.setDate(4, sqlDate);

            ps.executeBatch();

            return ps;
        });
    }

    @Override
    public void removeFriend(UserEntity requester, UserEntity addressee) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(USERDATA_JDBC_URL));
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "DELETE FROM friendship WHERE requester_id = ? AND addressee_id = ? AND status = ?"
            );

            ps.setObject(1, requester.getId());
            ps.setObject(2, addressee.getId());
            ps.setObject(3, FriendshipStatus.ACCEPTED.name());

            ps.addBatch();
            ps.clearParameters();

            ps.setObject(1, addressee.getId());
            ps.setObject(2, requester.getId());
            ps.setObject(3, FriendshipStatus.ACCEPTED.name());

            ps.executeBatch();

            return ps;
        });
    }

    @Override
    public void remove(UserEntity user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(USERDATA_JDBC_URL));
        jdbcTemplate.update(
                "DELETE FROM \"user\" WHERE id = ?",
                user.getId()
        );
    }

    @Override
    public void removeAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(USERDATA_JDBC_URL));
        jdbcTemplate.update(
                "TRUNCATE TABLE \"user\" CASCADE"
        );
    }

}
