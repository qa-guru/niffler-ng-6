package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.BrowserExtension;
import guru.qa.niffler.jupiter.spend.Spend;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.enums.CurrencyValuesEnum.RUB;
import static guru.qa.niffler.testData.DataConstant.MAIN_PASSWORD;
import static guru.qa.niffler.testData.DataConstant.MAIN_USER;

@ExtendWith(BrowserExtension.class)
@Feature("UI:Работа с пользовательским профилем")
public class ProfileTest extends BaseTest {

  @Spend(
      category = "Обучение",
      description = "Обучение Advanced 2.0",
      username = MAIN_USER,
      amount = 84550,
      currency = RUB
  )
  @Test
  @Description("Проверка отображения архивных категорий")
  void checkPresentOfArchivedCategory(SpendJson spend) {
    final String newSpendingName = "Обучение Advanced 3.0";

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .doLogin(MAIN_USER, MAIN_PASSWORD)
        .editSpending(spend.description())
        .editSpendingDescription(newSpendingName);

    new MainPage().checkThatTableContains(newSpendingName);
  }
}