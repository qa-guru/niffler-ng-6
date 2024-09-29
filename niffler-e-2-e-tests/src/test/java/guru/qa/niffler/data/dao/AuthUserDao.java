package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;

import java.util.List;

public interface AuthUserDao {
    AuthUserEntity create(AuthUserEntity user);

    List<AuthUserEntity> findAll();

}
