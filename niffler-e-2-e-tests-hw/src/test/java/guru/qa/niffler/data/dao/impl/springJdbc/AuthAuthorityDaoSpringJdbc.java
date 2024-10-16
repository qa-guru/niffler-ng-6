package guru.qa.niffler.data.dao.impl.springJdbc;

import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthAuthorityDaoSpringJdbc implements AuthAuthorityDao {

    @Override
    public void create(AuthAuthorityEntity... authorityEntity) {

    }

    @Override
    public Optional<AuthAuthorityEntity> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public List<AuthAuthorityEntity> findByUserId(UUID userId) {
        return List.of();
    }

    @Override
    public void delete(AuthAuthorityEntity... entity) {

    }

}
