package guru.qa.niffler.test.web;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UsersDbClient;
import org.junit.jupiter.api.Test;


import java.util.Date;
import java.util.List;
import java.util.Optional;

import static guru.qa.niffler.utils.RandomDataUtils.randomCategoryName;
import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;
import static org.junit.jupiter.api.Assertions.*;

public class JdbcTest {
  static UsersDbClient usersDbClient = new UsersDbClient();
  private final SpendDbClient spendDbClient = new SpendDbClient();

  @Test
  void fullSpendAndCategoryLifecycleTest() {
    CategoryJson category = spendDbClient.createCategory(
            new CategoryJson(
                    null,
                    randomCategoryName(),
                    "duck",
                    false
            )
    );
    assertNotNull(category.id(), "ID новой категории не должен быть null");
    System.out.println("Созданная категория: " + category);

    CategoryJson updatedCategory = new CategoryJson(
            category.id(),
            "updated-" + category.name(),
            category.username(),
            category.archived()
    );
    CategoryJson resultCategory = spendDbClient.updateCategory(updatedCategory);
    assertEquals("updated-" + category.name(), resultCategory.name(), "Имя категории должно быть обновлено");
    System.out.println("Обновленная категория: " + resultCategory);

    SpendJson spend = spendDbClient.createSpend(
            new SpendJson(
                    null,
                    new Date(),
                    resultCategory,
                    CurrencyValues.RUB,
                    1500.0,
                    "spend-name-lifecycle-test",
                    "duck"
            )
    );
    assertNotNull(spend.id(), "ID новой траты не должен быть null");
    System.out.println("Созданная трата: " + spend);

    SpendJson updatedSpend = new SpendJson(
            spend.id(),
            spend.spendDate(),
            spend.category(),
            spend.currency(),
            2000.0,
            "updated-spend-description",
            spend.username()
    );

    SpendJson resultSpend = spendDbClient.updateSpend(updatedSpend);
    assertEquals(2000.0, resultSpend.amount(), "Сумма должна быть обновлена");
    assertEquals("updated-spend-description", resultSpend.description(), "Описание должно быть обновлено");
    System.out.println("Обновленная трата: " + resultSpend);

    Optional<SpendJson> foundSpendById = spendDbClient.findSpendById(resultSpend.id());
    assertTrue(foundSpendById.isPresent(), "Трата должна быть найдена по ID");
    System.out.println("Найденная трата по ID: " + foundSpendById.get());

    List<SpendJson> foundSpendsByDescription = spendDbClient.findSpendByUsernameAndDescription(resultSpend.username(), resultSpend.description());
    assertFalse(foundSpendsByDescription.isEmpty(), "Список трат не должен быть пустым для указанного пользователя и описания");
    System.out.println("Найденные траты по описанию: " + foundSpendsByDescription);

    Optional<CategoryJson> foundCategoryById = spendDbClient.findCategoryById(resultCategory.id());
    assertTrue(foundCategoryById.isPresent(), "Категория должна быть найдена по ID");
    System.out.println("Найденная категория по ID: " + foundCategoryById.get());

    Optional<CategoryJson> foundCategoryByNameAndUsername = spendDbClient.findCategoryByUsernameAndCategoryName(resultSpend.username(), resultCategory.name());
    assertTrue(foundCategoryByNameAndUsername.isPresent(), "Категория должна быть найдена по имени пользователя и имени категории");
    System.out.println("Найденная категория по имени пользователя и имени категории: " + foundCategoryByNameAndUsername.get());

    spendDbClient.deleteSpend(resultSpend);
    Optional<SpendJson> foundSpendAfterDeletion = spendDbClient.findSpendById(resultSpend.id());
    assertTrue(foundSpendAfterDeletion.isEmpty(), "Трата не должна быть найдена после удаления");
    System.out.println("Трата успешно удалена: " + resultSpend);

    spendDbClient.deleteCategory(resultCategory);
    Optional<CategoryJson> foundCategoryAfterDeletion = spendDbClient.findCategoryById(resultCategory.id());
    assertTrue(foundCategoryAfterDeletion.isEmpty(), "Категория не должна быть найдена после удаления");
    System.out.println("Категория успешно удалена: " + resultCategory);
  }

  @Test
  void springJdbcTest() {
    UserJson user = usersDbClient.createUser(
            randomUsername(),
            "12345"
    );
    usersDbClient.createIncomeInvitations(user, 1);
    usersDbClient.createOutcomeInvitations(user, 1);
    usersDbClient.createFriends(user, 1);
    System.out.println(user);
  }
}
