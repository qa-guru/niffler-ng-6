package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.jpa.EntityManagers.em;

@Slf4j
public class AuthUserRepositoryHibernate implements AuthUserRepository {

    private static final String AUTH_JDBC_URL = Config.getInstance().authJdbcUrl();
    private final EntityManager em = em(AUTH_JDBC_URL);
    private final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public AuthUserEntity create(AuthUserEntity user) {
        log.info("Creating user: {}", user);
        em.joinTransaction();
        em.persist(user.setPassword(pe.encode(user.getPassword())));        // User saved in db and updated user data
        return user;
    }

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        return Optional.ofNullable(
                em.find(AuthUserEntity.class, id)
        );
    }

    @Override
    public Optional<AuthUserEntity> findByUsername(String username) {
        try {
            return Optional.of(
                    em.createQuery("select u from UserEntity u where u.username =: username", AuthUserEntity.class)
                            .setParameter("username", username)
                            .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<AuthUserEntity> findAll() {
        return em.createQuery("SELECT u FROM UserEntity u", AuthUserEntity.class).getResultList();
    }

    @Override
    public void remove(AuthUserEntity user) {
        em.joinTransaction();
        em.remove(user);
    }

}
