package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.UserdataRepository;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class UserdataUserRepositoryJdbc implements UserdataRepository {
    private final Config CFG = Config.getInstance();

    @Override
    public UserEntity create(UserEntity user) {
        try (PreparedStatement userPs = holder(CFG.userDataJdbcUrl()).connection().prepareStatement(
                "INSERT INTO \"user\" (username, currency, firstname, surname, full_name, photo, photo_small) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS
        );
             PreparedStatement friendshipPs = holder(CFG.userDataJdbcUrl()).connection().prepareStatement(
                     "INSERT INTO \"friendship\" (requester_id, addressee_id, created_date, status) " +
                             "VALUES (?, ?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS
             )
        ) {
            userPs.setString(1, user.getUsername());
            userPs.setString(2, user.getCurrency().name());
            userPs.setString(3, user.getFirstname());
            userPs.setString(4, user.getSurname());
            userPs.setString(5, user.getFullname());
            userPs.setObject(6, user.getPhoto());
            userPs.setObject(7, user.getPhotoSmall());

            userPs.executeUpdate();

            final UUID generatedKey;

            try (ResultSet rs = userPs.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can't find id in ResultSet");
                }
            }

            user.setId(generatedKey);

            for (FriendshipEntity friendshipRequest : user.getFriendshipRequests()) {
                friendshipPs.setObject(1, friendshipRequest.getRequester());
                friendshipPs.setObject(2, friendshipRequest.getAddressee());
                friendshipPs.setDate(3, new java.sql.Date(friendshipRequest.getCreatedDate().getTime()));
                friendshipPs.setObject(4, friendshipRequest.getStatus());
                friendshipPs.addBatch();
                friendshipPs.clearParameters();
            }

            for (FriendshipEntity friendshipAddressee : user.getFriendshipAddressees()) {
                friendshipPs.setObject(1, friendshipAddressee.getRequester());
                friendshipPs.setObject(2, friendshipAddressee.getAddressee());
                friendshipPs.setDate(3, new java.sql.Date(friendshipAddressee.getCreatedDate().getTime()));
                friendshipPs.setObject(4, friendshipAddressee.getStatus());
                friendshipPs.addBatch();
                friendshipPs.clearParameters();
            }

            friendshipPs.executeBatch();

            return user;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        try (PreparedStatement ps = holder(CFG.userDataJdbcUrl()).connection().prepareStatement(
                "select * from \"user\" u join \"friendship\" f ON u.id = f.requester_id or u.id = f.addressee_id " +
                        "where id = ?"
        )) {
            ps.setObject(1, id);
            ps.execute();

            UserEntity user = null;
            List<FriendshipEntity> requestList = new ArrayList<>();
            List<FriendshipEntity> addresseeList = new ArrayList<>();
            try (ResultSet rs = ps.getResultSet()) {
                while (rs.next()) {
                    if (user == null) {
                        user = getUserEntity(rs);
                    }

                    FriendshipEntity fe = new FriendshipEntity();
                    fe.setRequester(user);
                    fe.setAddressee(user);
                    fe.setCreatedDate(rs.getDate("created_date"));
                    fe.setStatus(FriendshipStatus.valueOf(rs.getString("status")));

                    if (fe.getRequester().getId().equals(user.getId())) {
                        requestList.add(fe);
                    } else {
                        addresseeList.add(fe);
                    }
                }

                if (user == null) {
                    return Optional.empty();
                } else {
                    user.setFriendshipRequests(requestList);
                    user.setFriendshipAddressees(addresseeList);
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addInvitation(UserEntity requester, UserEntity addressee) {
        try (PreparedStatement ps = holder(CFG.userDataJdbcUrl()).connection().prepareStatement(
                "insert into \"friendship\" (requester_id, addressee_id, created_date, status) " +
                        "VALUES (?, ?, ?, ?)"
        )) {
            Date now = Date.valueOf(LocalDate.now());

            ps.setObject(1, requester.getId());
            ps.setObject(2, addressee.getId());
            ps.setDate(3, now);
            ps.setString(4, FriendshipStatus.PENDING.name());
            ps.addBatch();

            ps.setObject(1, addressee.getId());
            ps.setObject(2, requester.getId());
            ps.setDate(3, now);
            ps.setString(4, FriendshipStatus.PENDING.name());
            ps.addBatch();

            ps.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addFriend(UserEntity requester, UserEntity addressee) {
        try (PreparedStatement ps = holder(CFG.userDataJdbcUrl()).connection().prepareStatement(
                "insert into \"friendship\"  (requester_id, addressee_id, created_date, status) " +
                        "VALUES (?, ?, ?, ?)"
        )) {
            Date now = Date.valueOf(LocalDate.now());

            ps.setObject(1, requester.getId());
            ps.setObject(2, addressee.getId());
            ps.setDate(3, now);
            ps.setString(4, FriendshipStatus.ACCEPTED.name());
            ps.addBatch();

            ps.setObject(1, addressee.getId());
            ps.setObject(2, requester.getId());
            ps.setDate(3, now);
            ps.setString(4, FriendshipStatus.ACCEPTED.name());
            ps.addBatch();

            ps.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private UserEntity getUserEntity(ResultSet rs) throws SQLException {
        UserEntity user = new UserEntity();
        user.setId(rs.getObject("id", UUID.class));
        user.setFirstname(rs.getString("firstname"));
        user.setFullname(rs.getString("full_name"));
        user.setUsername(rs.getString("username"));
        user.setSurname(rs.getString("surname"));
        user.setPhoto(rs.getBytes("photo"));
        user.setPhotoSmall(rs.getBytes("photo_small"));
        user.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
        return user;
    }
}

