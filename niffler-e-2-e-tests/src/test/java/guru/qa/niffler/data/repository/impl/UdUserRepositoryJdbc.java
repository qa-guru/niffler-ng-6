package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.UdUserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class UdUserRepositoryJdbc implements UdUserRepository {

    private static final Config CFG = Config.getInstance();

    @Override
    public UserEntity create(UserEntity user) {
        try (PreparedStatement userPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "INSERT INTO \"user\" (username, currency, firstname, surname, photo, photo_small, full_name) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {

            userPs.setString(1, user.getUsername());
            userPs.setString(2, user.getCurrency().name());
            userPs.setString(3, user.getFirstname());
            userPs.setString(4, user.getSurname());
            userPs.setBytes(5, user.getPhoto());
            userPs.setBytes(6, user.getPhotoSmall());
            userPs.setString(7, user.getFullname());
            userPs.executeUpdate();


            final UUID generatedKey;
            try (ResultSet rs = userPs.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Не удалось получить сгенерированный ID");
                }
            }
            user.setId(generatedKey);
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addFriendships(UserEntity user) {
        try (PreparedStatement friendshipPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "INSERT INTO \"friendship\" (requester_id, addressee_id, status, created_date) VALUES (?, ?, ?, ?)")) {

            for (FriendshipEntity friendship : user.getFriendshipRequests()) {
                friendshipPs.setObject(1, user.getId()); // requester_id
                friendshipPs.setObject(2, friendship.getAddressee().getId()); // addressee_id
                friendshipPs.setString(3, friendship.getStatus().name());
                friendshipPs.setDate(4, new java.sql.Date(friendship.getCreatedDate().getTime()));
                friendshipPs.addBatch();
            }
            friendshipPs.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}