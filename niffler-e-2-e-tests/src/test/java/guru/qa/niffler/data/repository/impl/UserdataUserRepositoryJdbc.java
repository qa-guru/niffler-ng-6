package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.data.dao.UdUserDao;
import guru.qa.niffler.data.dao.impl.UdUserDaoJdbc;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.UserdataUserRepository;

import java.util.Optional;
import java.util.UUID;

public class UserdataUserRepositoryJdbc implements UserdataUserRepository {

  private final UdUserDao udUserDao = new UdUserDaoJdbc();

  @Override
  public UserEntity create(UserEntity user) {
    return udUserDao.create(user);
  }

  @Override
  public UserEntity update(UserEntity user) {
    return udUserDao.update(user);
  }

  @Override
  public Optional<UserEntity> findById(UUID id) {
    return udUserDao.findById(id);
  }

  @Override
  public Optional<UserEntity> findByUsername(String username) {
    return udUserDao.findByUsername(username);
  }

  @Override
  public void addFriendshipRequest(UserEntity requester, UserEntity addressee) {
    requester.addFriends(FriendshipStatus.PENDING, addressee);
    udUserDao.update(requester);
  }

  @Override
  public void addFriend(UserEntity requester, UserEntity addressee) {
    requester.addFriends(FriendshipStatus.ACCEPTED, addressee);
    addressee.addFriends(FriendshipStatus.ACCEPTED, requester);
    udUserDao.update(requester);
    udUserDao.update(addressee);
  }
}
