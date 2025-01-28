package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.mapper.AuthUserEntityRowMapper;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class AuthUserRepositorySpringJdbc implements AuthUserRepository {

    private static final Config CFG = Config.getInstance();

    private final AuthUserDao authUserDao = new AuthUserDaoSpringJdbc();
    private final AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoSpringJdbc();

    @Override
    public AuthUserEntity create(AuthUserEntity userAuth) {
        authUserDao.create(userAuth);
        authAuthorityDao.create(userAuth.getAuthorities().toArray(new AuthAuthorityEntity[0]));
        return userAuth;
    }

    @Override
    public AuthUserEntity update(AuthUserEntity authUser) {
        authUserDao.update(authUser);
        return authUser;
    }

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "SELECT * FROM public.user u INNER JOIN public.authority a ON u.id = a.user_id WHERE id=?",
                        AuthUserEntityRowMapper.instance,
                        id
                )
        );
    }

    @Override
    public Optional<AuthUserEntity> findByUsername(String username) {
        Optional<AuthUserEntity> userEntity = authUserDao.findByUsername(username);
        userEntity.ifPresent(authUserEntity -> authUserEntity.addAuthorities(
                authAuthorityDao.findByUserId(
                        authUserEntity.getId()).toArray(new AuthAuthorityEntity[0]))
        );
        return userEntity;
    }

    @Override
    public List<AuthUserEntity> findAll() {

        List<AuthUserEntity> userEntityList = authUserDao.findAll();
        if (!userEntityList.isEmpty()) {
            for (AuthUserEntity authUserEntity : userEntityList) {
                authUserEntity.addAuthorities(authUserEntity.getAuthorities().toArray(new AuthAuthorityEntity[0]));
            }
        }
        return userEntityList;
    }

    @Override
    public void remove(AuthUserEntity authUser) {
        authUserDao.delete(authUser);
        for (AuthAuthorityEntity authAuthority : authAuthorityDao.findByUserId(authUser.getId())) {
            authAuthorityDao.delete(authAuthority);
        }
    }
}
