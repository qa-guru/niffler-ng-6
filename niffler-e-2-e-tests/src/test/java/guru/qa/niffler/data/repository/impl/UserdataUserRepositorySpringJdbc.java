package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.mapper.UdUserEntityResultSetExtractor;
import guru.qa.niffler.data.repository.UserdataRepository;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public class UserdataUserRepositorySpringJdbc implements UserdataRepository {
    private final Config CFG = Config.getInstance();

    @Override
    public UserEntity create(UserEntity user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userDataJdbcUrl()));
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
                    PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO \"user\" (username, currency, firstname, surname, full_name, photo, photo_small) " +
                                    "VALUES (?, ?, ?, ?, ?, ?, ?)",
                            Statement.RETURN_GENERATED_KEYS
                    );

                    ps.setString(1, user.getUsername());
                    ps.setString(2, user.getCurrency().name());
                    ps.setString(3, user.getFirstname());
                    ps.setString(4, user.getSurname());
                    ps.setString(5, user.getFullname());
                    ps.setObject(6, user.getPhoto());
                    ps.setObject(7, user.getPhotoSmall());

                    return ps;
                },
                kh
        );
        final UUID generatedKey;
        generatedKey = (UUID) kh.getKeys().get("id");
        user.setId(generatedKey);


        return user;
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userDataJdbcUrl()));
        return Optional.ofNullable(
                jdbcTemplate.query(
                        "select * from \"user\" u join \"friendship\" f on u.id = f.requester_id or u.id = f.addressee_id " +
                                "where id = ?",
                        UdUserEntityResultSetExtractor.instance,
                        id
                )
        );
    }

    @Override
    public void addInvitation(UserEntity requester, UserEntity addressee) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userDataJdbcUrl()));
        Date now = Date.valueOf(LocalDate.now());

        jdbcTemplate.batchUpdate(
                "insert into \"friendship\"  (requester_id, addressee_id, created_date, status) " +
                        "VALUES (?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        if (i == 0) {
                            ps.setObject(1, requester.getId());
                            ps.setObject(2, addressee.getId());
                        } else {
                            ps.setObject(1, addressee.getId());
                            ps.setObject(2, requester.getId());
                        }
                        ps.setDate(3, now);
                        ps.setString(4, FriendshipStatus.PENDING.name());
                    }

                    @Override
                    public int getBatchSize() {
                        return 2;
                    }
                });
    }


    @Override
    public void addFriend(UserEntity requester, UserEntity addressee) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userDataJdbcUrl()));
        Date now = Date.valueOf(LocalDate.now());

        jdbcTemplate.batchUpdate(
                "insert into \"friendship\"  (requester_id, addressee_id, created_date, status) " +
                        "VALUES (?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        if (i == 0) {
                            ps.setObject(1, requester.getId());
                            ps.setObject(2, addressee.getId());
                        } else {
                            ps.setObject(1, addressee.getId());
                            ps.setObject(2, requester.getId());
                        }
                        ps.setDate(3, now);
                        ps.setString(4, FriendshipStatus.ACCEPTED.name());
                    }

                    @Override
                    public int getBatchSize() {
                        return 2;
                    }
                });
    }
}
