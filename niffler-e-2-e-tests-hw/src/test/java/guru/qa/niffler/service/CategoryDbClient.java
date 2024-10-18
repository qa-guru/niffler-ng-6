package guru.qa.niffler.service;

import guru.qa.niffler.model.CategoryJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryDbClient {

    CategoryJson create(CategoryJson categoryJson);

    Optional<CategoryJson> findById(UUID id);

    Optional<CategoryJson> findByUsernameAndName(String username, String name);

    List<CategoryJson> findAllByUsername(String username);

    List<CategoryJson> findAll();

    void delete(CategoryJson categoryJson);

}
