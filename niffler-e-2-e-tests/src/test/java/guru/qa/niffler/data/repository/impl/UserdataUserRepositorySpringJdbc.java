package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.mapper.UdUserEntityRowMapper;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserdataUserRepositorySpringJdbc implements UserdataUserRepository {

    private static final Config CFG = Config.getInstance();
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));

    @Override
    public UserEntity create(UserEntity user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO \"user\" (username, currency, firstname, surname, photo, photo_small, full_name) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getCurrency().name());
            ps.setString(3, user.getFirstname());
            ps.setString(4, user.getSurname());
            ps.setBytes(5, user.getPhoto());
            ps.setBytes(6, user.getPhotoSmall());
            ps.setString(7, user.getFullname());
            return ps;
        }, keyHolder);
        final UUID generatedKey = (UUID) keyHolder.getKeys().get("id");
        user.setId(generatedKey);
        return user;
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        String sql = "SELECT * FROM \"user\" WHERE id = ?";
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(sql, UdUserEntityRowMapper.instance, id)
        );
    }

    @Override
    public Optional<UserEntity> findByUsername(String userName) {
        String sql = "SELECT * FROM \"user\" WHERE username = ?";
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(sql, UdUserEntityRowMapper.instance, userName)
        );
    }


    @Override
    public UserEntity update(UserEntity user) {
        String sql = "UPDATE \"user\" SET currency = ?, firstname = ?, surname = ?, photo = ?, " +
                "photo_small = ?, full_name = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                user.getCurrency().name(),
                user.getFirstname(),
                user.getSurname(),
                user.getPhoto(),
                user.getPhotoSmall(),
                user.getFullname(),
                user.getId()
        );
        return user;
    }

    @Override
    public void sendInvitation(UserEntity requester, UserEntity addressee) {
        String sql = "INSERT INTO \"friendship\" (requester_id, addressee_id, status, created_date) " +
                "VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                requester.getId(),
                addressee.getId(),
                FriendshipStatus.PENDING.name(),
                new java.sql.Date(System.currentTimeMillis())
        );
    }

    @Override
    public void addFriend(UserEntity requester, UserEntity addressee) {
        String sql = "INSERT INTO \"friendship\" (requester_id, addressee_id, status, created_date) VALUES (?, ?, ?, ?)";
        // Создаем параметры для вставки дружбы в двух направлениях
        Object[] firstPair = {
                requester.getId(),
                addressee.getId(),
                FriendshipStatus.ACCEPTED.name(),
                new java.sql.Date(System.currentTimeMillis())
        };
        Object[] secondPair = {
                addressee.getId(),
                requester.getId(),
                FriendshipStatus.ACCEPTED.name(),
                new java.sql.Date(System.currentTimeMillis())
        };
        // Собираем параметры в список для batchUpdate
        List<Object[]> batchArgs = Arrays.asList(firstPair, secondPair);
        // Выполняем batch вставку
        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    @Override
    public void remove(UserEntity user) {
        String sql = "DELETE FROM \"user\" WHERE id = ?";
        jdbcTemplate.update(sql, user.getId());
    }
}