package guru.qa.niffler.test.db.springJdbc;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.model.UserModel;
import guru.qa.niffler.service.impl.springJdbc.CategoryDbClientSpringJdbc;
import guru.qa.niffler.utils.CategoryUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class CategorySpringJdbcTest {

    @Test
    void shouldCreateNewCategoryTest(@CreateNewUser UserModel user) {
        assertNotNull(
                new CategoryDbClientSpringJdbc()
                        .create(CategoryUtils.generateForUser(user.getUsername()))
                        .getId());
    }

    @Test
    void shouldGetCategoryByIdTest(@CreateNewUser(categories = @Category) UserModel user) {
        assertNotNull(
                new CategoryDbClientSpringJdbc()
                        .findById(user.getCategories().getFirst().getId()));
    }

    @Test
    void shouldGetCategoryByUsernameAndNameTest(@CreateNewUser(categories = @Category) UserModel user) {
        assertNotNull(
                new CategoryDbClientSpringJdbc()
                        .findByUsernameAndName(
                                user.getUsername(),
                                user.getCategories().getFirst().getName()));
    }

    @Test
    void shouldGetAllCategoriesByUsernameTest(@CreateNewUser(categories = @Category) UserModel user) {
        assertFalse(
                new CategoryDbClientSpringJdbc()
                        .findAllByUsername(user.getUsername())
                        .isEmpty());
    }

    @Test
    void shouldGetAllCategories(
            @CreateNewUser(categories = @Category) UserModel user1,
            @CreateNewUser(categories = @Category) UserModel user2
    ) {
        var allCategories = new CategoryDbClientSpringJdbc().findAll();
        assertAll("Should contains users categories", () -> {
            assertTrue(allCategories.contains(user1.getCategories().getFirst()));
            assertTrue(allCategories.contains(user2.getCategories().getFirst()));
        });
    }

    @Test
    void shouldRemoveCategoryTest(@CreateNewUser(categories = @Category) UserModel user) {
        CategoryDbClientSpringJdbc categoryClient = new CategoryDbClientSpringJdbc();
        categoryClient.delete(user.getCategories().getFirst());
        assertTrue(categoryClient.findAllByUsername(user.getUsername()).isEmpty());
    }

}
