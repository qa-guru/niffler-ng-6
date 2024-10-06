package guru.qa.niffler.data.repository;


import guru.qa.niffler.data.entity.userdata.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserdataRepository {
    UserEntity create(UserEntity user);
    Optional<UserEntity> findById(UUID id);
    void addInvitation(UserEntity requester, UserEntity addressee);
    void addFriend(UserEntity requester, UserEntity addressee);
}
