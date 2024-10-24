package guru.qa.niffler.test.db.jdbc;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.model.UserModel;
import guru.qa.niffler.service.CategoryDbClient;
import guru.qa.niffler.service.impl.jdbc.CategoryDbClientJdbc;
import guru.qa.niffler.service.impl.springJdbc.CategoryDbClientSpringJdbc;
import guru.qa.niffler.utils.CategoryUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class CategoryJdbcTest {
    
    private final CategoryDbClient categoryDbClient = new CategoryDbClientJdbc();
    
    @Test
    void shouldCreateNewCategoryTest(@CreateNewUser UserModel user) {
        assertNotNull(
                categoryDbClient
                        .create(CategoryUtils.generateForUser(user.getUsername()))
                        .getId());
    }

    @Test
    void shouldGetCategoryByIdTest(@CreateNewUser(categories = @Category) UserModel user) {
        assertNotNull(
                categoryDbClient
                        .findById(user.getCategories().getFirst().getId()));
    }

    @Test
    void shouldGetCategoryByUsernameAndNameTest(@CreateNewUser(categories = @Category) UserModel user) {
        assertNotNull(
                categoryDbClient
                        .findByUsernameAndName(
                                user.getUsername(),
                                user.getCategories().getFirst().getName()));
    }

    @Test
    void shouldGetAllCategoriesByUsernameTest(@CreateNewUser(categories = @Category) UserModel user) {
        assertFalse(
                categoryDbClient
                        .findAllByUsername(user.getUsername())
                        .isEmpty());
    }

    @Test
    void shouldGetAllCategoriesTest(
            @CreateNewUser(categories = @Category) UserModel user1,
            @CreateNewUser(categories = @Category) UserModel user2
    ) {
        var allCategories = categoryDbClient.findAll();
        assertAll("Should contains users categories", () -> {
            assertTrue(allCategories.contains(user1.getCategories().getFirst()));
            assertTrue(allCategories.contains(user2.getCategories().getFirst()));
        });
    }

    @Test
    void shouldRemoveCategoryTest(@CreateNewUser(categories = @Category) UserModel user) {
        categoryDbClient.delete(user.getCategories().getFirst());
        assertTrue(categoryDbClient.findAllByUsername(user.getUsername()).isEmpty());
    }

}
