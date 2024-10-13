package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.CategoryEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryDao {

    CategoryEntity create(CategoryEntity category);

    Optional<CategoryEntity> findById(UUID id);

    Optional<CategoryEntity> findByUsernameAndName(String username, String name);

    List<CategoryEntity> findAllByUsername(String username);

    void delete(CategoryEntity category);

}
