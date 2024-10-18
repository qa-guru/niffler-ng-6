package guru.qa.niffler.test.db.jdbc;

import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.UserModel;
import guru.qa.niffler.service.impl.jdbc.CategoryDbClientJdbc;
import guru.qa.niffler.utils.CategoryUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class CategoryJdbcTest {

    private final CategoryDbClientJdbc categoryDbClient = new CategoryDbClientJdbc();

    @Test
    void shouldCreateNewCategoryTest(@CreateNewUser UserModel user) {
        CategoryJson category = categoryDbClient
                .create(CategoryUtils.generate().setUsername(user.getUsername()));
        assertNotNull(category.getId());
    }

    @Test
    void shouldGetCategoryByIdTest(@CreateNewUser UserModel user) {
        var category = categoryDbClient.create(CategoryUtils.generate().setUsername(user.getUsername()));
        var foundedCategory = categoryDbClient.findById(category.getId()).orElse(new CategoryJson());
        assertEquals(category, foundedCategory);
    }

    @Test
    void shouldGetCategoryByUsernameAndNameTest(@CreateNewUser UserModel user) {
        var category = categoryDbClient.create(CategoryUtils.generate().setUsername(user.getUsername()));
        var foundedCategory = categoryDbClient.findByUsernameAndName(
                        user.getUsername(),
                        category.getName())
                .orElse(new CategoryJson());
        assertEquals(category, foundedCategory);
    }

    @Test
    void shouldGetAllSpendsByUsernameTest(@CreateNewUser UserModel user) {

        var category1 = categoryDbClient.create(CategoryUtils.generate().setUsername(user.getUsername()));
        var category2 = categoryDbClient.create(CategoryUtils.generate().setUsername(user.getUsername()));
        List<CategoryJson> categories = categoryDbClient.findAllByUsername(user.getUsername());

        assertTrue(categories.contains(category1));
        assertTrue(categories.contains(category2));

    }

    @Test
    void shouldRemoveCategoryTest(@CreateNewUser UserModel user) {
        CategoryJson category = categoryDbClient.create(CategoryUtils.generate().setUsername(user.getUsername()));
        categoryDbClient.delete(category);
        assertEquals(0, categoryDbClient.findAllByUsername(user.getUsername()).size());
    }

}
