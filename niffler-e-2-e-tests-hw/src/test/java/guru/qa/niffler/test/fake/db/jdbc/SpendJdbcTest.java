package guru.qa.niffler.test.fake.db.jdbc;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.model.rest.UserJson;
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
    void shouldCreateNewSpendTest(@CreateNewUser(categories = @Category) UserJson user) {
        var category = user.getTestData().getCategories().getFirst();
        assertNotNull(spendClient
                .create(SpendUtils.generateForUser(user.getUsername()).setCategory(category))
                .getId());
    }

    @Test
    void shouldGetSpendByIdTest(@CreateNewUser(spendings = @Spending) UserJson user) {
        assertNotNull(spendClient
                .findById(user.getTestData().getSpendings().getFirst().getId()));
    }

    @Test
    void shouldGetSpendByUsernameAndDescriptionTest(@CreateNewUser(spendings = @Spending) UserJson user) {
        assertNotNull(spendClient
                .findAllByUsernameAndDescription(
                        user.getUsername(),
                        user.getTestData().getSpendings().getFirst().getDescription()));
    }

    @Test
    void shouldGetAllSpendingsByUsernameTest(@CreateNewUser(spendings = @Spending) UserJson user) {
        assertFalse(spendClient
                .findAllByUsername(user.getUsername())
                .isEmpty());
    }

    @Test
    void shouldGetAllSpendingsTest(
            @CreateNewUser(spendings = @Spending) UserJson user1,
            @CreateNewUser(spendings = @Spending) UserJson user2
    ) {
        var allSpendings = spendClient.findAll();
        assertAll("Should contains users spendings", () -> {
            assertTrue(allSpendings.contains(user1.getTestData().getSpendings().getFirst()));
            assertTrue(allSpendings.contains(user2.getTestData().getSpendings().getFirst()));
        });
    }

    @Test
    void shouldRemoveSpendTest(@CreateNewUser(spendings = @Spending) UserJson user) {
        spendClient.remove(user.getTestData().getSpendings().getFirst());
        assertTrue(spendClient
                .findAllByUsername(user.getUsername()).isEmpty());
    }


    @Test
    void shouldCreateNewCategoryTest(@CreateNewUser UserJson user) {
        assertNotNull(spendClient
                .createCategory(CategoryUtils.generateForUser(user.getUsername()))
                .getId());
    }

    @Test
    void shouldGetCategoryByIdTest(@CreateNewUser(categories = @Category) UserJson user) {
        assertNotNull(spendClient
                .findCategoryById(user.getTestData().getCategories().getFirst().getId()));
    }

    @Test
    void shouldGetCategoryByUsernameAndNameTest(@CreateNewUser(categories = @Category) UserJson user) {
        assertNotNull(spendClient
                .findCategoryByUsernameAndName(
                        user.getUsername(),
                        user.getTestData().getCategories().getFirst().getName()));
    }

    @Test
    void shouldGetAllCategoriesByUsernameTest(@CreateNewUser(categories = @Category) UserJson user) {
        assertFalse(spendClient
                .findAllCategoriesByUsername(user.getUsername())
                .isEmpty());
    }

    @Test
    void shouldGetAllCategories(
            @CreateNewUser(categories = @Category) UserJson user1,
            @CreateNewUser(categories = @Category) UserJson user2
    ) {
        var allCategories = spendClient.findAllCategories();
        assertAll("Should contains users categories", () -> {
            assertTrue(allCategories.contains(user1.getTestData().getCategories().getFirst()));
            assertTrue(allCategories.contains(user2.getTestData().getCategories().getFirst()));
        });
    }

    @Test
    void shouldRemoveCategoryTest(@CreateNewUser(categories = @Category) UserJson user) {
        spendClient.removeCategory(user.getTestData().getCategories().getFirst());
        assertTrue(spendClient.findAllCategoriesByUsername(user.getUsername()).isEmpty());
    }

}
