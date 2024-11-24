package guru.qa.niffler.data.repository.impl.springJdbc;

import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.springJdbc.UserdataUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.ex.UserNotFoundException;
import guru.qa.niffler.model.rest.UserJson;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class UserdataUserRepositorySpringJdbc implements UserdataUserRepository {

    private final UserdataUserDao userdataUserDao = new UserdataUserDaoSpringJdbc();

    @Override
    public @Nonnull UserEntity create(UserEntity user) {
        return userdataUserDao.create(user);
    }

    @Override
    public @Nonnull Optional<UserEntity> findById(UUID id) {
        return userdataUserDao.findById(id);
    }

    @Override
    public @Nonnull Optional<UserEntity> findByUsername(String username) {
        return userdataUserDao.findByUsername(username);
    }

    @Override
    public @Nonnull List<UserEntity> findAll() {
        return userdataUserDao.findAll();
    }

    @Override
    public @Nonnull UserEntity update(UserEntity user) {
        return userdataUserDao.update(user);
    }

    @Override
    public List<UserEntity> getIncomeInvitations(UserEntity user) {
        return userdataUserDao.getIncomeInvitations(user);
    }

    @Override
    public List<UserEntity> getOutcomeInvitations(UserEntity user) {
        return userdataUserDao.getOutcomeInvitations(user);
    }

    @Override
    public List<UserEntity> getFriends(UserEntity user) {
        return userdataUserDao.getFriends(user);
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
        userdataUserDao.removeFriend(requester, addressee);
    }

    @Override
    public void remove(UserEntity user) {
        userdataUserDao.remove(user);
    }

    public void removeAll() {
        userdataUserDao.removeAll();
    }

}
