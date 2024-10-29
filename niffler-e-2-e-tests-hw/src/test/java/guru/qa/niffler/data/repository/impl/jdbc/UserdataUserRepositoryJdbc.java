package guru.qa.niffler.data.repository.impl.jdbc;

import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.jdbc.UserdataUserDaoJdbc;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.UserdataUserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserdataUserRepositoryJdbc implements UserdataUserRepository {

    private final UserdataUserDao userdataUserDao = new UserdataUserDaoJdbc();

    @Override
    public UserEntity create(UserEntity user) {
        return userdataUserDao.create(user);
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        return userdataUserDao.findById(id);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return userdataUserDao.findByUsername(username);
    }

    @Override
    public List<UserEntity> findAll() {
        return userdataUserDao.findAll();
    }

    @Override
    public UserEntity update(UserEntity user) {
        return userdataUserDao.update(user);
    }

    @Override
    public void sendInvitation(UserEntity requester, UserEntity addressee) {
        userdataUserDao.sendInvitation(requester, addressee);
    }

    @Override
    public void removeInvitation(UserEntity requester, UserEntity addressee) {
        userdataUserDao.removeInvitation(requester, addressee);
    }

    @Override
    public void addFriend(UserEntity requester, UserEntity addressee) {
        userdataUserDao.addFriend(requester, addressee);
    }

    @Override
    public void removeFriend(UserEntity requester, UserEntity addressee) {
        userdataUserDao.addFriend(requester, addressee);
    }

    @Override
    public void remove(UserEntity user) {
        userdataUserDao.remove(user);
    }

}
