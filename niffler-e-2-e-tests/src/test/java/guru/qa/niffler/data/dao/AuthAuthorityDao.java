package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;

import java.util.List;
import java.util.UUID;

public interface AuthAuthorityDao {
    void create(AuthAuthorityEntity...authAuthority);

    List<AuthAuthorityEntity> findByUserId(UUID userId);

    List<AuthAuthorityEntity> findAll();

    void delete(AuthAuthorityEntity authAuthority);
}