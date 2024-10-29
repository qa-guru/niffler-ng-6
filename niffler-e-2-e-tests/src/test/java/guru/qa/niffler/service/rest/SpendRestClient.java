package guru.qa.niffler.service.rest;


import guru.qa.niffler.api.CategoriesApiClient;
import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendClient;

import java.util.List;

public class SpendRestClient implements SpendClient {
    private final CategoriesApiClient categoriesApiClient = new CategoriesApiClient();
    private final SpendApiClient spendApiClient = new SpendApiClient();

    @Override
    public SpendJson create(SpendJson spend) {
        return spendApiClient.createSpend(spend);
    }

    @Override
    public CategoryJson createCategory(CategoryJson category) {
        return categoriesApiClient.addCategory(category);
    }

    @Override
    public CategoryJson updateCategoryArchivedStatus(CategoryJson category) {
        return categoriesApiClient.updateCategory(category);
    }

    @Override
    public List<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String name) {
        return categoriesApiClient.getCategories(username, name);
    }

    @Override
    public void deleteCategory(CategoryJson category) {
        throw new RuntimeException("Not support method for rest");
    }
}
