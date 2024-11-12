package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.jpa.EntityManagers.em;

@ParametersAreNonnullByDefault
public class SpendRepositoryHibernate implements SpendRepository {

  private static final Config CFG = Config.getInstance();

  private final EntityManager entityManager = em(CFG.spendJdbcUrl());

  @Nonnull
  @Override
  public SpendEntity create(SpendEntity spend) {
    entityManager.joinTransaction();
    entityManager.persist(spend);
    return spend;
  }

  @Nonnull
  @Override
  public SpendEntity update(SpendEntity spend) {
    entityManager.joinTransaction();
    return entityManager.merge(spend);
  }

  @Nonnull
  @Override
  public CategoryEntity createCategory(CategoryEntity category) {
    entityManager.joinTransaction();
    entityManager.persist(category);
    return category;
  }

  @NotNull
  @Override
  public CategoryEntity updateCategory(CategoryEntity category) {
    entityManager.joinTransaction();
    return entityManager.merge(category);
  }

  @Nonnull
  @Override
  public Optional<CategoryEntity> findCategoryById(UUID id) {
    return Optional.ofNullable(
        entityManager.find(CategoryEntity.class, id)
    );
  }

  @Nonnull
  @Override
  public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String name) {
    try {
      return Optional.of(
          entityManager.createQuery("select c from CategoryEntity c where c.username =: username and c.name =: name", CategoryEntity.class)
              .setParameter("username", username)
              .setParameter("name", name)
              .getSingleResult()
      );
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }

  @Nonnull
  @Override
  public Optional<SpendEntity> findById(UUID id) {
    return Optional.ofNullable(
        entityManager.find(SpendEntity.class, id)
    );
  }

  @Nonnull
  @Override
  public Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String description) {
    try {
      return Optional.of(
          entityManager.createQuery("select s from SpendEntity s where s.username =: username and s.description =: description", SpendEntity.class)
              .setParameter("username", username)
              .setParameter("description", description)
              .getSingleResult()
      );
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }

  @Override
  public void remove(SpendEntity spend) {
    entityManager.joinTransaction();
    entityManager.remove(entityManager.contains(spend) ? spend : entityManager.merge(spend));
  }

  @Override
  public void removeCategory(CategoryEntity category) {
    entityManager.joinTransaction();
    entityManager.remove(entityManager.contains(category) ? category : entityManager.merge(category));
  }
}
