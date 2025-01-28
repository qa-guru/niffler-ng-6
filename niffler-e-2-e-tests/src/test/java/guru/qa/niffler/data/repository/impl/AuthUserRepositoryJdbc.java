package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.mapper.AuthUserEntityRowMapper;
import guru.qa.niffler.data.repository.AuthUserRepository;

import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static guru.qa.niffler.data.tpl.Connections.holder;

@ParametersAreNonnullByDefault
public class AuthUserRepositoryJdbc implements AuthUserRepository {

    private static final Config CFG = Config.getInstance();

    private final AuthUserDao authUserDao = new AuthUserDaoJdbc();
    private final AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoJdbc();

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
        Optional<AuthUserEntity> authUser = authUserDao.findById(id);
        authUser.ifPresent(authUserEntity ->
                authUserEntity.addAuthorities(
                        authAuthorityDao.findByUserId(authUserEntity.getId()).toArray(new AuthAuthorityEntity[0])
                ));
        return authUser;
    }

    @Override
    public Optional<AuthUserEntity> findByUsername(String username) {
        Optional<AuthUserEntity> authUser = authUserDao.findByUsername(username);
        authUser.ifPresent(authUserEntity ->
                authUserEntity.addAuthorities(
                        authAuthorityDao.findByUserId(authUserEntity.getId()).toArray(new AuthAuthorityEntity[0])
                ));
        return authUser;
    }

    @Override
    public List<AuthUserEntity> findAll() {
        List<AuthUserEntity> userEntityList = authUserDao.findAll();
        if (!userEntityList.isEmpty()) {
            for (AuthUserEntity authUserEntity : userEntityList) {
                authUserEntity.addAuthorities(
                        authAuthorityDao.findByUserId(authUserEntity.getId()).toArray(new AuthAuthorityEntity[0])
                );
            }
        }
        return userEntityList;
    }

    @Override
    public void remove(AuthUserEntity authUser) {
        try (PreparedStatement userPs = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "DELETE public.user WHERE id=? ");
             PreparedStatement authorityPs = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                     "DELETE FROM public.authority WHERE  user_id=?")
        ) {
            userPs.setObject(1, authUser.getId());
            int resExecuteUpdate = userPs.executeUpdate();
            if (resExecuteUpdate == 0) {
                throw new SQLException("Can't find deleted user");
            }
            for (AuthAuthorityEntity ae : authUser.getAuthorities()) {
                authorityPs.setObject(1, ae.getUser());
                int resAuthorityExecuteUpdate = authorityPs.executeUpdate();
                if (resAuthorityExecuteUpdate == 0) {
                    throw new SQLException("Can't find deleted authority");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
