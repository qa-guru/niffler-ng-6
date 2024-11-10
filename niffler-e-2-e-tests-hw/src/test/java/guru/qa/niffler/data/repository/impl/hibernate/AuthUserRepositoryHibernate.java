package guru.qa.niffler.data.repository.impl.hibernate;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.jpa.EntityManagers.em;

@Slf4j
@ParametersAreNonnullByDefault
public class AuthUserRepositoryHibernate implements AuthUserRepository {

    private static final String AUTH_JDBC_URL = Config.getInstance().authJdbcUrl();
    private final EntityManager em = em(AUTH_JDBC_URL);
    private final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public @Nonnull AuthUserEntity create(AuthUserEntity user) {
        em.joinTransaction();
        em.persist(user.setPassword(pe.encode(user.getPassword())));
        return user;
    }

    @Override
    public @Nonnull Optional<AuthUserEntity> findById(UUID id) {
        return Optional.ofNullable(
                em.find(AuthUserEntity.class, id)
        );
    }

    @Override
    public @Nonnull Optional<AuthUserEntity> findByUsername(String username) {
        try {
            return Optional.of(
                    em.createQuery("SELECT u FROM AuthUserEntity u WHERE u.username =: username", AuthUserEntity.class)
                            .setParameter("username", username)
                            .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public @Nonnull List<AuthUserEntity> findAll() {
        return em.createQuery("SELECT u FROM AuthUserEntity u", AuthUserEntity.class).getResultList();
    }

    @Override
    public @Nonnull AuthUserEntity update(AuthUserEntity user) {
        em.joinTransaction();
        return em.merge(user.setPassword(pe.encode(user.getPassword())));
    }

    @Override
    public void remove(AuthUserEntity user) {
        em.joinTransaction();
        var authorities = user.getAuthorities();
        user = em.merge(user.setAuthorities(Collections.emptyList()));
        authorities.forEach(a -> {
            a = em.merge(a.setUser(null));
            em.remove(a);
        });
        em.remove(user);
    }

}
