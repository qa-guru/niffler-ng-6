package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.FriendshipDao;
import guru.qa.niffler.data.dao.UserDao;
import guru.qa.niffler.data.dao.impl.FriendshipDaoJdbc;
import guru.qa.niffler.data.dao.impl.UserDaoJdbc;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.mapper.UserEntityRowMapper;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.model.CurrencyValues;

import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

@ParametersAreNonnullByDefault
public class UserdataUserRepositoryJdbc implements UserdataUserRepository {
    private static final Config CFG = Config.getInstance();

    private final UserDao userDao = new UserDaoJdbc();
    private final FriendshipDao friendshipDao = new FriendshipDaoJdbc();

    @Override
    public UserEntity create(UserEntity authUser) {
        userDao.createUser(authUser);
        return authUser;
    }

    @Override
    public UserEntity update(UserEntity authUser) {
        userDao.update(authUser);
        return authUser;
    }

    @Override
    public void addFriend(UserEntity requester, UserEntity addressee) {
        friendshipDao.createFriendshipAccepted(requester, addressee);
    }

    @Override
    public void addInvitation(UserEntity requester, UserEntity addressee) {
        friendshipDao.createFriendshipPending(requester, addressee);
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        return userDao.findById(id);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public List<UserEntity> findAll() {
        return userDao.findAll();
    }

    @Override
    public void remove(UserEntity user) {
        user.removeFriends();
    }
}