package guru.qa.niffler.test.fake.db.hibernate;

import com.github.javafaker.Faker;
import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.SpendClient;
import guru.qa.niffler.service.db.impl.hibernate.SpendDbClientHibernate;
import guru.qa.niffler.utils.CategoryUtils;
import guru.qa.niffler.utils.SpendUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class SpendHibernateTest {

    private final SpendClient spendClient = new SpendDbClientHibernate();

    @Test
    void shouldCreateNewSpendTest(
            @CreateNewUser UserJson user
    ) {
        var category = spendClient.createCategory(CategoryUtils.generateForUser(user.getUsername()));
        assertNotNull(spendClient
                .create(SpendUtils.generateForUser(user.getUsername()).setCategory(category))
                .getId());
    }

    @Test
    void shouldGetSpendByIdTest(
            @CreateNewUser UserJson user
    ) {
        var category = spendClient.createCategory(CategoryUtils.generateForUser(user.getUsername()));
        var spend = spendClient.create(SpendUtils.generateForUser(user.getUsername()).setCategory(category));
        assertNotNull(spendClient
                .findById(spend.getId()));
    }

    @Test
    void shouldGetFirstSpendByUsernameAndDescriptionTest(
            @CreateNewUser UserJson user
    ) {
        var category = spendClient.createCategory(CategoryUtils.generateForUser(user.getUsername()));
        var spend = spendClient.create(SpendUtils.generateForUser(user.getUsername()).setCategory(category));

        var foundedSpend = spendClient.findFirstSpendByUsernameAndDescription(user.getUsername(), spend.getDescription());
        assertTrue(foundedSpend.isPresent());
    }

    @Test
    void shouldGetSpendsByUsernameAndDescriptionTest(
            @CreateNewUser UserJson user1,
            @CreateNewUser UserJson user2
    ) {
        String spendDesc = new Faker().lorem().characters(5, 20);

        var spend1User1 = spendClient.create(
                SpendUtils.generateForUser(user1.getUsername())
                        .setCategory(spendClient
                                .createCategory(CategoryUtils.generateForUser(user1.getUsername())))
                        .setDescription(spendDesc));
        var spend2User1 = spendClient.create(
                SpendUtils.generateForUser(user1.getUsername())
                        .setCategory(spendClient
                                .createCategory(CategoryUtils.generateForUser(user1.getUsername())))
                        .setDescription(spendDesc));
        var spend3User1 = spendClient.create(
                SpendUtils.generateForUser(user1.getUsername())
                        .setCategory(spendClient
                                .createCategory(CategoryUtils.generateForUser(user1.getUsername()))));
        var spendUser2 = spendClient.create(
                SpendUtils.generateForUser(user2.getUsername())
                        .setCategory(spendClient
                                .createCategory(CategoryUtils.generateForUser(user2.getUsername())))
                        .setDescription(spendDesc));

        var spends = spendClient.findAllByUsernameAndDescription(user1.getUsername(), spendDesc);
        Assertions.assertAll("Spends with same username and description should exist, others - not",
                () -> assertTrue(spends.contains(spend1User1)),
                () -> assertTrue(spends.contains(spend2User1)),
                () -> assertFalse(spends.contains(spend3User1)),
                () -> assertFalse(spends.contains(spendUser2))
        );
    }

    @Test
    void shouldGetAllSpendingsByUsernameTest(
            @CreateNewUser UserJson user1,
            @CreateNewUser UserJson user2
    ) {

        String spendName = new Faker().lorem().characters(5, 20);

        var spend1User1 = spendClient.create(
                SpendUtils.generateForUser(user1.getUsername())
                        .setCategory(spendClient
                                .createCategory(CategoryUtils.generateForUser(user1.getUsername())))
                        .setDescription(spendName));
        var spend2User1 = spendClient.create(
                SpendUtils.generateForUser(user1.getUsername())
                        .setCategory(spendClient
                                .createCategory(CategoryUtils.generateForUser(user1.getUsername()))));
        var spendUser2 = spendClient.create(
                SpendUtils.generateForUser(user2.getUsername())
                        .setCategory(spendClient
                                .createCategory(CategoryUtils.generateForUser(user2.getUsername())))
                        .setDescription(spendName));

        var spends = spendClient.findAllByUsername(user1.getUsername());
        Assertions.assertAll("Spends with same username should exist, others - not",
                () -> assertTrue(spends.contains(spend1User1)),
                () -> assertTrue(spends.contains(spend2User1)),
                () -> assertFalse(spends.contains(spendUser2))
        );

    }

    @Test
    void shouldGetAllSpendingsTest(
            @CreateNewUser UserJson user1,
            @CreateNewUser UserJson user2
    ) {
        var spend1 = spendClient.create(SpendUtils.generateForUser(user1.getUsername()));
        var spend2 = spendClient.create(SpendUtils.generateForUser(user2.getUsername()));
        var spends = spendClient.findAll();
        assertAll("Should contains all users spendings", () -> {
            assertTrue(spends.contains(spend1));
            assertTrue(spends.contains(spend2));
        });
    }

    @Test
    void shouldRemoveSpendTest(
            @CreateNewUser UserJson user
    ) {

        var spend = spendClient.create(SpendUtils.generateForUser(user.getUsername()));
        spendClient.remove(spend);
        assertTrue(spendClient
                .findAllByUsername(user.getUsername()).isEmpty());
    }


    @Test
    void shouldCreateNewCategoryTest(
            @CreateNewUser UserJson user
    ) {
        assertNotNull(spendClient
                .createCategory(CategoryUtils.generateForUser(user.getUsername()))
                .getId());
    }

    @Test
    void shouldGetCategoryByIdTest(
            @CreateNewUser UserJson user
    ) {
        var category = spendClient.createCategory(
                CategoryUtils.generateForUser(user.getUsername()));
        assertTrue(
                spendClient.findCategoryById(category.getId())
                        .isPresent());
    }

    @Test
    void shouldGetCategoryByUsernameAndNameTest(
            @CreateNewUser UserJson user
    ) {
        var category = spendClient.createCategory(
                CategoryUtils.generateForUser(user.getUsername()));
        assertTrue(
                spendClient.findCategoryByUsernameAndName(category.getUsername(), category.getName())
                        .isPresent());
    }

    @Test
    void shouldGetAllCategoriesByUsernameTest(
            @CreateNewUser UserJson user1,
            @CreateNewUser UserJson user2
    ) {
        var category1User1 = spendClient.createCategory(
                CategoryUtils.generateForUser(user1.getUsername()));
        var category2User1 = spendClient.createCategory(
                CategoryUtils.generateForUser(user1.getUsername()));
        var categoryUser2 = spendClient.createCategory(
                CategoryUtils.generateForUser(user2.getUsername()));
        var categories = spendClient.findAllCategoriesByUsername(user1.getUsername());
        Assertions.assertAll("Categories with same username should exist, others - not",
                () -> assertTrue(categories.contains(category1User1)),
                () -> assertTrue(categories.contains(category2User1)),
                () -> assertFalse(categories.contains(categoryUser2))
        );
    }

    @Test
    void shouldGetAllCategories(
            @CreateNewUser UserJson user1,
            @CreateNewUser UserJson user2
    ) {
        var category1 = spendClient.createCategory(CategoryUtils.generateForUser(user1.getUsername()));
        var category2 = spendClient.createCategory(CategoryUtils.generateForUser(user2.getUsername()));
        var categories = spendClient.findAllCategories();
        assertAll("Should contains users categories", () -> {
            assertTrue(categories.contains(category1));
            assertTrue(categories.contains(category2));
        });
    }

    @Test
    void shouldRemoveCategoryTest(@CreateNewUser UserJson user) {
        var category = spendClient.createCategory(CategoryUtils.generateForUser(user.getUsername()));
        spendClient.removeCategory(category);
        assertTrue(spendClient.findAllCategoriesByUsername(user.getUsername()).isEmpty());
    }

}
