package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.userdata.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserdataUserDao {

    UserEntity create(UserEntity user);

    Optional<UserEntity> findById(UUID id);

    Optional<UserEntity> findByUsername(String username);

    List<UserEntity> findAll();

    UserEntity update(UserEntity user);

    void sendInvitation(UserEntity requester, UserEntity addressee);

    void removeInvitation(UserEntity requester, UserEntity addressee);

    void addFriend(UserEntity requester, UserEntity addressee);

    void removeFriend(UserEntity requester, UserEntity addressee);

    void remove(UserEntity user);

    void removeAll();

}
