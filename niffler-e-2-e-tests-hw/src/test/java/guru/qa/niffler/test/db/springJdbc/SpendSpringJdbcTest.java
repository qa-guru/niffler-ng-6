package guru.qa.niffler.test.db.springJdbc;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.model.UserModel;
import guru.qa.niffler.service.impl.springJdbc.SpendDbClientSpringJdbc;
import guru.qa.niffler.utils.SpendUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class SpendSpringJdbcTest {

    @Test
    void shouldCreateNewSpendTest(@CreateNewUser(categories = @Category) UserModel user) {
        var category = user.getCategories().getFirst();
        assertNotNull(
                new SpendDbClientSpringJdbc()
                        .create(SpendUtils.generateForUser(user.getUsername()).setCategory(category))
                        .getId());
    }

    @Test
    void shouldGetSpendByIdTest(@CreateNewUser(spendings = @Spending) UserModel user) {
        assertNotNull(
                new SpendDbClientSpringJdbc()
                        .findById(user.getSpendings().getFirst().getId()));
    }

    @Test
    void shouldGetSpendByUsernameAndDescriptionTest(@CreateNewUser(spendings = @Spending) UserModel user) {
        assertNotNull(
                new SpendDbClientSpringJdbc()
                        .findByUsernameAndDescription(
                                user.getUsername(),
                                user.getSpendings().getFirst().getDescription()));
    }

    @Test
    void shouldGetAllSpendingsByUsernameTest(@CreateNewUser(spendings = @Spending) UserModel user) {
        assertFalse(
                new SpendDbClientSpringJdbc()
                        .findAllByUsername(user.getUsername())
                        .isEmpty());
    }

    @Test
    void shouldGetAllSpendings(
            @CreateNewUser(spendings = @Spending) UserModel user1,
            @CreateNewUser(spendings = @Spending) UserModel user2
    ) {
        var allSpendings = new SpendDbClientSpringJdbc().findAll();
        assertAll("Should contains users spendings", () -> {
            assertTrue(allSpendings.contains(user1.getSpendings().getFirst()));
            assertTrue(allSpendings.contains(user2.getSpendings().getFirst()));
        });
    }

    @Test
    void shouldRemoveSpendTest(@CreateNewUser(spendings = @Spending) UserModel user) {
        SpendDbClientSpringJdbc spendClient = new SpendDbClientSpringJdbc();
        spendClient.delete(user.getSpendings().getFirst());
        assertTrue(spendClient.findAllByUsername(user.getUsername()).isEmpty());
    }


}
