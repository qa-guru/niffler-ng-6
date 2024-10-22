package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.DisabledByIssue;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.MainPage;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.enums.CurrencyValuesEnum.RUB;
import static guru.qa.niffler.testData.DataConstant.MAIN_PASSWORD;
import static guru.qa.niffler.testData.DataConstant.MAIN_USER;

@Feature("UI:Работа с пользовательскими тратами")
@WebTest
public class SpendingWebTest extends BaseTest {


  @User(
      username = MAIN_USER,
      spendings =  @Spending(
          category = "Обучение",
          description = "Обучение Advanced 2.0",
          amount = 84550,
          currency = RUB
      )
  )
  @Test
  @DisabledByIssue("3")
  @Description("Проверка возможности редактирования трат")
  void categoryDescriptionShouldBeEditedByTableAction(SpendJson spend) {
    final String newSpendingName = "Обучение Advanced 3.0";

    logIntoSystem(MAIN_USER, MAIN_PASSWORD)
        .editSpending(spend.description())
        .editSpendingDescription(newSpendingName);

    new MainPage().checkThatTableContains(newSpendingName);
  }

  @User(
      username = MAIN_USER,
      categories = @Category(
          isArchived = true
      )
  )
  @Test
  @Description("Проверка отсутствия архивных категорий")
  void checkPresentOfArchivedCategory(CategoryJson category) {
    logIntoSystem(MAIN_USER, MAIN_PASSWORD)
        .goToCreateNewSpendingPage()
        .checkCategoryIsNotPresent(category.name());
  }

  @User(
      username = MAIN_USER,
      categories = @Category(
          isArchived = false
      )
  )
  @Test
  @Description("Проверка отображения архивных категорий")
  void checkPresentOfActiveCategory(CategoryJson category) {
    logIntoSystem(MAIN_USER, MAIN_PASSWORD)
        .goToCreateNewSpendingPage()
        .checkCategoryIsPresent(category.name());
  }
}