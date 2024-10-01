package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.userdata.UserEntity;

import java.util.List;

public interface UdUserDao {

    UserEntity create(UserEntity user);

    List<UserEntity> findAll();

}