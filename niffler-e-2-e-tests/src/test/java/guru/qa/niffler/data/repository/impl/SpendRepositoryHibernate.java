package guru.qa.niffler.data.repository.impl;

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
    private static final Config CFG = Config.getInstance();
    private final EntityManager entityManager = em(CFG.spendJdbcUrl());

    @Override
    public SpendEntity create(SpendEntity spend) {
        entityManager.joinTransaction();
        entityManager.persist(spend);
        return spend;
    }

    @Override
    public SpendEntity update(SpendEntity spend) {
        entityManager.joinTransaction();
        return entityManager.merge(spend);
    }

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        entityManager.joinTransaction();
        entityManager.persist(category);
        return category;
    }

    @Override
    public CategoryEntity updateCategory(CategoryEntity category) {
        entityManager.joinTransaction();
        return entityManager.merge(category);
    }

    @Override
    public Optional<CategoryEntity> findCategoryById(UUID id) {
        return Optional.ofNullable(
                entityManager.find(CategoryEntity.class, id)
        );
    }

    @Override
    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String name) {
        try {
            CategoryEntity category = entityManager.createQuery(
                            "SELECT c FROM CategoryEntity c WHERE c.username = :username AND c.name = :name", CategoryEntity.class)
                    .setParameter("username", username)
                    .setParameter("name", name)
                    .getSingleResult();
            return Optional.of(category);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<SpendEntity> findById(UUID id) {
        return Optional.ofNullable(
                entityManager.find(SpendEntity.class, id)
        );
    }

    @Override
    public List<SpendEntity> findByUsernameAndSpendDescription(String username, String description) {
        return entityManager.createQuery(
                        "SELECT s FROM SpendEntity s WHERE s.username = :username AND s.description = :description", SpendEntity.class)
                .setParameter("username", username)
                .setParameter("description", description)
                .getResultList();
    }

    @Override
    public void remove(SpendEntity spend) {
        entityManager.joinTransaction();
        SpendEntity attachedSpend = entityManager.contains(spend) ? spend : entityManager.merge(spend);
        entityManager.remove(attachedSpend);
    }

    @Override
    public void removeCategory(CategoryEntity category) {
        entityManager.joinTransaction();
        CategoryEntity attachedCategory = entityManager.contains(category) ? category : entityManager.merge(category);
        entityManager.remove(attachedCategory);
    }
}