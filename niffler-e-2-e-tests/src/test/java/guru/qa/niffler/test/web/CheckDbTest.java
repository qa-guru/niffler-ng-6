package guru.qa.niffler.test.web;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.CategoryDbClient;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UserDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WebTest
@Disabled
public class CheckDbTest {

    //Прошу это не проверять. Написано для себя, скорее всего удалю чуть позже
    @Test
    void createSpendTest() {
        SpendDbClient spendDbClient = new SpendDbClient();

        SpendJson spend = spendDbClient.createSpend(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(null,
                                RandomDataUtils.randomCategoryName(),
                                "missa",
                                false),
                        CurrencyValues.RUB,
                        100.0,
                        "test desc",
                        "missa"
                )
        );
        System.out.println(spend);
    }

    @Test
    void findCategoryByIdTest() {
        CategoryDbClient categoryDbClient = new CategoryDbClient();
        Optional<CategoryEntity> foundCategory = categoryDbClient.findCategoryById(UUID.fromString("c5718446-fdd1-48d7-9f6a-3aeea17808cf"));

        System.out.println(foundCategory.get().getName());
    }

    @Test
    void findCategoryByUsernameAndCategoryNameTest() {
        CategoryDbClient categoryDbClient = new CategoryDbClient();

        Optional<CategoryEntity> categoryOptional = categoryDbClient.findCategoryByUsernameAndCategoryName(
                "kisa", "Barbecue Ribs");

        if (categoryOptional.isPresent()) {
            CategoryJson categoryJson = CategoryJson.fromEntity(categoryOptional.get());
            System.out.println(categoryJson);
        } else {
            System.out.println("Category not found");
        }
    }

    @Test
    void findAllCategoriesByUsernameTest() {
        CategoryDbClient categoryDbClient = new CategoryDbClient();
        List<CategoryEntity> categories = categoryDbClient.findAllCategoriesByUsername("kisa");

        String firstCategoryName = categories.get(0).getName();
        System.out.println("First category name: " + firstCategoryName);
        categories.stream().anyMatch(category -> "Barbecue Ribs".equals(category.getName()));
    }

    @Test
    void deleteCategoryTest() {
        CategoryDbClient categoryDbClient = new CategoryDbClient();
        CategoryEntity categoryToDelete = new CategoryEntity();
        categoryToDelete.setUsername("kissa");
        categoryToDelete.setName("testetst");
        categoryToDelete.setArchived(false);

        CategoryEntity createdCategory = categoryDbClient.createCategory(categoryToDelete);
        categoryDbClient.deleteCategory(createdCategory);

        Optional<CategoryEntity> deletedCategory = categoryDbClient.findCategoryByUsernameAndCategoryName(
                createdCategory.getUsername(), createdCategory.getName());
        assertFalse(deletedCategory.isPresent(), "Expected category to be deleted, but it was found");
    }

    @Test
    void findSpendByIdTest() {
        SpendDbClient spendDbClient = new SpendDbClient();
        Optional<SpendEntity> foundSpend = spendDbClient.findSpendById(UUID.fromString("35f9059b-e298-4754-8925-6cb2b52f65bc"));
        System.out.println(foundSpend.get().getDescription());
    }

    @Test
    void findAllSpendsByUsernameTest() {
        SpendDbClient spendDbClient = new SpendDbClient();
        List<SpendEntity> spends = spendDbClient.findAllSpendsByUsername("lissa");
        assertEquals(7, spends.size(), "Expected to find 7 spends for the user");
    }

    @Test
    void deleteSpendTest() {
        SpendDbClient spendDbClient = new SpendDbClient();
        SpendJson createdSpend = spendDbClient.createSpend(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(null,
                                RandomDataUtils.randomCategoryName(),
                                "missa",
                                false),
                        CurrencyValues.RUB,
                        100.0,
                        "test desc",
                        "missa"
                )
        );
        System.out.println(createdSpend);
        spendDbClient.deleteSpend(SpendEntity.fromJson(createdSpend));
    }

    @Test
    void createUserTest() {
        UserDbClient userDbClient = new UserDbClient();
        UserEntity user = new UserEntity();
        String userName = RandomDataUtils.randomUsername();
        user.setUsername(userName);
        user.setFirstname("Test");
        user.setSurname("User");
        user.setFullname("Test User");
        user.setCurrency(CurrencyValues.USD);

        UserEntity createdUser = userDbClient.createUser(user);

        assertEquals(userName, createdUser.getUsername(), "Expected username to match");
    }

    @Test
    void findUserByIdTest() {
        UserDbClient userDbClient = new UserDbClient();
        UUID userId = UUID.fromString("2af3b94a-222e-4499-ac04-d4aa4047c0f0");

        Optional<UserEntity> foundUser = userDbClient.findUserById(userId);

        System.out.println(foundUser.get().getUsername());
        assertTrue(foundUser.isPresent(), "Expected to find a user with given ID");
        assertEquals(userId, foundUser.get().getId(), "Expected user ID to match");
    }

    @Test
    void findUserByUsernameTest() {
        UserDbClient userDbClient = new UserDbClient();
        String username = "vito.satterfield";

        Optional<UserEntity> foundUser = userDbClient.findUserByUsername(username);

        System.out.println(foundUser.get().getId());
        assertTrue(foundUser.isPresent(), "Expected to find a user with given username");
        assertEquals(username, foundUser.get().getUsername(), "Expected username to match");
    }

    @Test
    void deleteUserTest() {
        UserDbClient userDbClient = new UserDbClient();
        UserEntity user = new UserEntity();
        user.setUsername(RandomDataUtils.randomUsername());
        user.setFirstname("Delete");
        user.setSurname("User");
        user.setFullname("Delete User");
        user.setCurrency(CurrencyValues.EUR);

        UserEntity createdUser = userDbClient.createUser(user);
        userDbClient.deleteUser(createdUser);

        Optional<UserEntity> deletedUser = userDbClient.findUserById(createdUser.getId());
        assertFalse(deletedUser.isPresent(), "Expected user to be deleted");
    }
}
