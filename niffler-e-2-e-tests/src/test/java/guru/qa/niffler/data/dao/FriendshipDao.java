package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.UserJson;

public interface FriendshipDao {

    public void createFriendshipPending(UserEntity requester, UserEntity addressee);

    public void createFriendshipAccepted(UserEntity requester, UserEntity addressee);
}
