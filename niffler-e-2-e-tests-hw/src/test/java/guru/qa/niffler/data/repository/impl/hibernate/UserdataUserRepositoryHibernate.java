package guru.qa.niffler.data.repository.impl.hibernate;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.jpa.EntityManagers;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.mapper.UserMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class UserdataUserRepositoryHibernate implements UserdataUserRepository {

    private static final String USERDATA_JDBC_URL = Config.getInstance().userdataJdbcUrl();
    private static final UserMapper userMapper = new UserMapper();
    private final EntityManager em = EntityManagers.em(USERDATA_JDBC_URL);

    @Override
    public @Nonnull UserEntity create(UserEntity user) {
        em.joinTransaction();
        em.persist(user);       // User saved in db and updated user data
        return user;
    }

    @Override
    public @Nonnull Optional<UserEntity> findById(UUID id) {
        return Optional.ofNullable(em.find(UserEntity.class, id));
    }

    @Override
    public @Nonnull Optional<UserEntity> findByUsername(String username) {
        try {
            return Optional.of(em.createQuery("SELECT u FROM UserEntity u WHERE u.username =: username", UserEntity.class)
                    .setParameter("username", username)
                    .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public @Nonnull List<UserEntity> findAll() {
        return em.createQuery("SELECT u FROM UserEntity u", UserEntity.class).getResultList();
    }

    @Override
    public @Nonnull UserEntity update(UserEntity user) {
        em.joinTransaction();
        return em.merge(user);
    }

    @Override
    public List<UserEntity> getIncomeInvitations(UserEntity user) {
        var hql = """
                SELECT new guru.qa.niffler.data.repository.impl.hibernate.UserdataUserRepositoryHibernate.FriendDTO(
                    u.id
                    , u.username
                    , u.currency
                    , u.firstName
                    , u.surname
                    , u.fullName)
                FROM
                    UserEntity u
                INNER JOIN
                    FriendshipEntity fr
                ON
                    fr.requester = u.id
                WHERE
                    fr.addressee =: user
                AND
                    fr.status =: status""";
        return em.createQuery(hql, FriendDTO.class)
                .setParameter("user", user)
                .setParameter("status", FriendshipStatus.PENDING)
                .getResultList()
                .stream()
                .map(UserdataUserRepositoryHibernate::mapFriendDTOToUserEntity)
                .toList();
    }

    @Override
    public List<UserEntity> getOutcomeInvitations(UserEntity user) {
        var hql = """
                SELECT new guru.qa.niffler.data.repository.impl.hibernate.UserdataUserRepositoryHibernate.FriendDTO(
                    u.id
                    , u.username
                    , u.currency
                    , u.firstName
                    , u.surname
                    , u.fullName)
                FROM
                    UserEntity u
                INNER JOIN
                    FriendshipEntity fr
                ON
                    fr.addressee = u.id
                WHERE
                    fr.requester =: user
                AND
                    fr.status =: status""";
        return em.createQuery(hql, FriendDTO.class)
                .setParameter("user", user)
                .setParameter("status", FriendshipStatus.PENDING)
                .getResultList()
                .stream()
                .map(UserdataUserRepositoryHibernate::mapFriendDTOToUserEntity)
                .toList();
    }

    @Override
    public List<UserEntity> getFriends(UserEntity user) {
        var hql = """
                SELECT new guru.qa.niffler.data.repository.impl.hibernate.UserdataUserRepositoryHibernate.FriendDTO(
                    u.id
                    , u.username
                    , u.currency
                    , u.firstName
                    , u.surname
                    , u.fullName)
                FROM
                    UserEntity u
                INNER JOIN
                    FriendshipEntity fr
                ON
                    fr.addressee = u.id
                WHERE
                    fr.requester =: user
                AND
                    fr.status =: status""";
        return em.createQuery(hql, FriendDTO.class)
                .setParameter("user", user)
                .setParameter("status", FriendshipStatus.ACCEPTED)
                .getResultList()
                .stream()
                .map(UserdataUserRepositoryHibernate::mapFriendDTOToUserEntity)
                .toList();
    }

    @Override
    public void sendInvitation(UserEntity requester, UserEntity addressee) {
        em.joinTransaction();
        addressee.addFriends(FriendshipStatus.PENDING, requester);
    }

    @Override
    public void removeInvitation(UserEntity requester, UserEntity addressee) {
        em.joinTransaction();
        addressee.removeInvites(requester);
    }

    @Override
    public void addFriend(UserEntity requester, UserEntity addressee) {
        em.joinTransaction();
        requester.addFriends(FriendshipStatus.ACCEPTED, addressee);
        addressee.addFriends(FriendshipStatus.ACCEPTED, requester);
    }

    @Override
    public void removeFriend(UserEntity requester, UserEntity addressee) {
        em.joinTransaction();
        requester.removeFriends(addressee);
        addressee.removeFriends(requester);
    }

    @Override
    public void remove(UserEntity user) {
        em.joinTransaction();
        em.remove(user);
    }

    @Override
    public void removeAll() {
        em.joinTransaction();
        em.createNativeQuery("TRUNCATE TABLE \"user\" CASCADE").executeUpdate();
    }

    private static UserEntity mapFriendDTOToUserEntity(FriendDTO friendDTO) {
        return UserEntity.builder()
                .id(friendDTO.id)
                .username(friendDTO.username)
                .currency(friendDTO.currency)
                .firstName(friendDTO.firstName)
                .surname(friendDTO.surname)
                .fullName(friendDTO.fullName)
                .build();
    }

    public record FriendDTO(
            UUID id,
            String username,
            CurrencyValues currency,
            String firstName,
            String surname,
            String fullName
    ) {
    }

}
