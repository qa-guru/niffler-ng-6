package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryDao {
  CategoryEntity create(CategoryEntity category);

  Optional<CategoryEntity> findCategoryById(UUID id);

  List<CategoryEntity> findAllByUsername(String username);

  void deleteCategory(CategoryEntity spend);
}
