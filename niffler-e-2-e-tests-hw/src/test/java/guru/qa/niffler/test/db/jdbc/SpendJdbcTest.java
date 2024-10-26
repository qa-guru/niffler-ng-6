package guru.qa.niffler.test.db.jdbc;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.model.UserModel;
import guru.qa.niffler.service.SpendClient;
import guru.qa.niffler.service.db.impl.jdbc.SpendDbClientJdbc;
import guru.qa.niffler.utils.CategoryUtils;
import guru.qa.niffler.utils.SpendUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class SpendJdbcTest {

    private final SpendClient spendClient = new SpendDbClientJdbc();

    @Test
    void shouldCreateNewSpendTest(@CreateNewUser(categories = @Category) UserModel user) {
        var category = user.getCategories().getFirst();
        assertNotNull(spendClient
                .create(SpendUtils.generateForUser(user.getUsername()).setCategory(category))
                .getId());
    }

    @Test
    void shouldGetSpendByIdTest(@CreateNewUser(spendings = @Spending) UserModel user) {
        assertNotNull(spendClient
                .findById(user.getSpendings().getFirst().getId()));
    }

    @Test
    void shouldGetSpendByUsernameAndDescriptionTest(@CreateNewUser(spendings = @Spending) UserModel user) {
        assertNotNull(spendClient
                .findAllByUsernameAndDescription(
                        user.getUsername(),
                        user.getSpendings().getFirst().getDescription()));
    }

    @Test
    void shouldGetAllSpendingsByUsernameTest(@CreateNewUser(spendings = @Spending) UserModel user) {
        assertFalse(spendClient
                .findAllByUsername(user.getUsername())
                .isEmpty());
    }

    @Test
    void shouldGetAllSpendingsTest(
            @CreateNewUser(spendings = @Spending) UserModel user1,
            @CreateNewUser(spendings = @Spending) UserModel user2
    ) {
        var allSpendings = spendClient.findAll();
        assertAll("Should contains users spendings", () -> {
            assertTrue(allSpendings.contains(user1.getSpendings().getFirst()));
            assertTrue(allSpendings.contains(user2.getSpendings().getFirst()));
        });
    }

    @Test
    void shouldRemoveSpendTest(@CreateNewUser(spendings = @Spending) UserModel user) {
        spendClient.remove(user.getSpendings().getFirst());
        assertTrue(spendClient
                .findAllByUsername(user.getUsername()).isEmpty());
    }


    @Test
    void shouldCreateNewCategoryTest(@CreateNewUser UserModel user) {
        assertNotNull(spendClient
                .createCategory(CategoryUtils.generateForUser(user.getUsername()))
                .getId());
    }

    @Test
    void shouldGetCategoryByIdTest(@CreateNewUser(categories = @Category) UserModel user) {
        assertNotNull(spendClient
                .findCategoryById(user.getCategories().getFirst().getId()));
    }

    @Test
    void shouldGetCategoryByUsernameAndNameTest(@CreateNewUser(categories = @Category) UserModel user) {
        assertNotNull(spendClient
                .findCategoryByUsernameAndName(
                        user.getUsername(),
                        user.getCategories().getFirst().getName()));
    }

    @Test
    void shouldGetAllCategoriesByUsernameTest(@CreateNewUser(categories = @Category) UserModel user) {
        assertFalse(spendClient
                .findAllCategoriesByUsername(user.getUsername())
                .isEmpty());
    }

    @Test
    void shouldGetAllCategories(
            @CreateNewUser(categories = @Category) UserModel user1,
            @CreateNewUser(categories = @Category) UserModel user2
    ) {
        var allCategories = spendClient.findAllCategories();
        assertAll("Should contains users categories", () -> {
            assertTrue(allCategories.contains(user1.getCategories().getFirst()));
            assertTrue(allCategories.contains(user2.getCategories().getFirst()));
        });
    }

    @Test
    void shouldRemoveCategoryTest(@CreateNewUser(categories = @Category) UserModel user) {
        spendClient.removeCategory(user.getCategories().getFirst());
        assertTrue(spendClient.findAllCategoriesByUsername(user.getUsername()).isEmpty());
    }

}
