package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.userdata.UserEntity;

public interface UdUserRepository {
    UserEntity create(UserEntity user);
    void addFriendships(UserEntity user);
}