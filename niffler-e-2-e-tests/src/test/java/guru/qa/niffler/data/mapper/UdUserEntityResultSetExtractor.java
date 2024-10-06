package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.CurrencyValues;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UdUserEntityResultSetExtractor implements ResultSetExtractor<UserEntity> {

    public static final UdUserEntityResultSetExtractor instance = new UdUserEntityResultSetExtractor();

    private UdUserEntityResultSetExtractor() {
    }

    @Override
    public UserEntity extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<UUID, UserEntity> userEntityMap = new ConcurrentHashMap<>();
        UUID userId;
        UserEntity userEntity = null;
        List<FriendshipEntity> requestList = new ArrayList<>();
        List<FriendshipEntity> addressesList = new ArrayList<>();
        while (rs.next()) {
            userId = rs.getObject("id", UUID.class);
            userEntity = userEntityMap.computeIfAbsent(
                    userId,
                    key -> {
                        try {
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
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );

            FriendshipEntity entity = new FriendshipEntity();
            entity.setRequester(rs.getObject("requester_id", UserEntity.class));
            entity.setAddressee(rs.getObject("addressee_id", UserEntity.class));
            entity.setCreatedDate(rs.getDate("created_date"));
            entity.setStatus(FriendshipStatus.valueOf(rs.getString("status")));

            if (entity.getRequester().getId().equals(userId)) {
                requestList.add(entity);
            } else {
                addressesList.add(entity);
            }
        }
        if (userEntity != null) {
            userEntity.setFriendshipRequests(requestList);
            userEntity.setFriendshipAddressees(addressesList);
        }
        return userEntity;
    }
}
