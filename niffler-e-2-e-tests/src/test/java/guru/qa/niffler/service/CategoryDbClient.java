package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.imp.CategoryDaoJdbc;
import guru.qa.niffler.data.entity.category.CategoryEntity;
import guru.qa.niffler.model.CategoryJson;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CategoryDbClient {
  private final CategoryDao categoryDao = new CategoryDaoJdbc();

  public CategoryJson createCategory(CategoryJson category) {
    if (category.id() == null) {
      return CategoryJson.fromEntity(categoryDao.create(CategoryEntity.fromJson(category)));
    } else {
      return category;
    }
  }

  public List<CategoryJson> findAllByUsername(String username) {
    return categoryDao.findAllByUsername(username).stream()
        .map(CategoryJson::fromEntity)
        .collect(Collectors.toList());
  }

  public CategoryJson findCategoryById(UUID id) {
    return categoryDao.findCategoryById(id)
        .map(CategoryJson::fromEntity)
        .orElse(null);
  }

  public void deleteCategory(CategoryJson category) {
    categoryDao.deleteCategory(CategoryEntity.fromJson(category));
  }
}
