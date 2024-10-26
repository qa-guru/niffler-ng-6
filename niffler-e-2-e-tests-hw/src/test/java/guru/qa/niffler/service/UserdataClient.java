package guru.qa.niffler.service;

import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.model.UserModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserdataClient {

    UserModel create(UserModel userModel);

    Optional<UserModel> findById(UUID id);

    Optional<UserModel> findByUsername(String username);

    List<UserModel> findAll();

    void sendInvitation(UserModel requester, UserModel addressee, FriendshipStatus status);

    void addFriend(UserModel requester, UserModel addressee);

    void remove(UserModel userModel);

}
