package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.UserdataUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.UserdataUserRepository;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class UserdataUserRepositorySpringJdbc implements UserdataUserRepository {

  private final UserdataUserDao userdataUserDao = new UserdataUserDaoSpringJdbc();

  @Nonnull
  @Override
  public UserEntity create(UserEntity user) {
    return userdataUserDao.create(user);
  }

  @Nonnull
  @Override
  public UserEntity update(UserEntity user) {
    return userdataUserDao.update(user);
  }

  @Nonnull
  @Override
  public Optional<UserEntity> findById(UUID id) {
    return userdataUserDao.findById(id);
  }

  @Nonnull
  @Override
  public Optional<UserEntity> findByUsername(String username) {
    return userdataUserDao.findByUsername(username);
  }

  @Override
  public void addFriendshipRequest(UserEntity requester, UserEntity addressee) {
    requester.addFriends(FriendshipStatus.PENDING, addressee);
    userdataUserDao.update(requester);
  }

  @Override
  public void addFriend(UserEntity requester, UserEntity addressee) {
    requester.addFriends(FriendshipStatus.ACCEPTED, addressee);
    addressee.addFriends(FriendshipStatus.ACCEPTED, requester);
    userdataUserDao.update(requester);
    userdataUserDao.update(addressee);
  }
}
