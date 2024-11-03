package guru.qa.niffler.service;


import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.Optional;

public interface SpendClient {

    public SpendJson create(SpendJson spend);

    public CategoryJson createCategory(CategoryJson category);

    public CategoryJson updateCategoryArchivedStatus(CategoryJson category);

    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String name);

    public void deleteCategory(CategoryJson category);
}
