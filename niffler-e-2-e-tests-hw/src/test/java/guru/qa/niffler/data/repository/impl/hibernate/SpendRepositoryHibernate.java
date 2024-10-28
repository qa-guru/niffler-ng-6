package guru.qa.niffler.data.repository.impl.hibernate;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.jpa.EntityManagers.em;

public class SpendRepositoryHibernate implements SpendRepository {

    private static final String SPEND_JDBC_URL = Config.getInstance().spendJdbcUrl();

    private final EntityManager em = em(SPEND_JDBC_URL);

    @Override
    public SpendEntity create(SpendEntity spend) {
        em.joinTransaction();
        if (!em.contains(spend.getCategory())) {
            spend.setCategory(em.merge(spend.getCategory()));
        }
        em.persist(spend);
        return spend;
    }

    @Override
    public Optional<SpendEntity> findById(UUID id) {
        try {
            return Optional.of(
                    em.createQuery("SELECT s FROM SpendEntity s WHERE s.id =: id", SpendEntity.class)
                            .setParameter("id", id)
                            .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<SpendEntity> findAllByUsername(String username) {
        return em.createQuery("SELECT s FROM SpendEntity s WHERE s.username =: username", SpendEntity.class)
                .setParameter("username", username)
                .getResultList();
    }

    @Override
    public Optional<SpendEntity> findFirstSpendByUsernameAndDescription(String username, String description) {
        var spends = em.createQuery("SELECT s FROM SpendEntity s WHERE s.username =: username AND s.description =: description", SpendEntity.class)
                .setParameter("username", username)
                .setParameter("description", description)
                .getResultList();
        return Optional.ofNullable(spends.getFirst());

    }

    @Override
    public List<SpendEntity> findByUsernameAndDescription(String username, String description) {
        return em.createQuery("SELECT s FROM SpendEntity s WHERE s.username =: username AND s.description =: description", SpendEntity.class)
                .setParameter("username", username)
                .setParameter("description", description)
                .getResultList();

    }

    @Override
    public List<SpendEntity> findAll() {
        return em.createQuery("SELECT s FROM SpendEntity s", SpendEntity.class)
                .getResultList();
    }

    @Override
    public SpendEntity update(SpendEntity spend) {
        em.joinTransaction();
        return em.merge(spend);
    }

    @Override
    public void remove(SpendEntity spend) {
        em.joinTransaction();
        spend = em.merge(spend.setCategory(null));
        em.remove(spend);
    }

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        em.joinTransaction();
        em.persist(category);
        return category;
    }

    @Override
    public Optional<CategoryEntity> findCategoryById(UUID id) {
        try {
            return Optional.of(
                    em.createQuery("SELECT c FROM CategoryEntity c WHERE c.id =: id", CategoryEntity.class)
                            .setParameter("id", id)
                            .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<CategoryEntity> findCategoryByUsernameAndName(String username, String name) {
        try {
            return Optional.of(
                    em.createQuery("SELECT c FROM CategoryEntity c WHERE c.username =: username AND c.name =: name", CategoryEntity.class)
                            .setParameter("username", username)
                            .setParameter("name", name)
                            .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<CategoryEntity> findAllCategoriesByUsername(String username) {
        return em.createQuery("SELECT c FROM CategoryEntity c WHERE c.username =: username", CategoryEntity.class)
                .setParameter("username", username)
                .getResultList();
    }

    @Override
    public List<CategoryEntity> findAllCategories() {
        return em.createQuery("SELECT c FROM CategoryEntity c", CategoryEntity.class)
                .getResultList();
    }

    @Override
    public CategoryEntity updateCategory(CategoryEntity category) {
        em.joinTransaction();
        return em.merge(category);
    }

    @Override
    public void removeCategory(CategoryEntity category) {
        em.joinTransaction();

        CategoryEntity managedCategory = em.contains(category) ? category : em.merge(category);

        em.createQuery("SELECT s FROM SpendEntity s WHERE s.category = :category", SpendEntity.class)
                .setParameter("category", managedCategory)
                .getResultList()
                .forEach(spend -> {
                    spend.setCategory(null);
                    em.merge(spend);
                });

        // Удаляем категорию
        em.remove(managedCategory);
    }

}
