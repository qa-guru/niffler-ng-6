package guru.qa.niffler.service.rest;


import guru.qa.niffler.api.CategoriesApiClient;
import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendClient;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@ParametersAreNonnullByDefault
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
    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username,  String name) {
        List<CategoryJson> categiries = categoriesApiClient.getCategories(username);
        return Optional.of(categiries.stream().filter(x-> name.equals(x.name())).findFirst().get());
    }

    @Override
    public void deleteCategory(CategoryJson category) {
        throw new RuntimeException("Not support method for rest");
    }
}
