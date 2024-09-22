package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.BrowserExtension;
import guru.qa.niffler.jupiter.Spend;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.enums.CurrencyValues.RUB;

@ExtendWith(BrowserExtension.class)
public class SpendingWebTest {

  private static final Config CFG = Config.getInstance();

  @Spend(
      category = "Обучение",
      description = "Обучение Advanced 2.0",
      username = "max",
      amount = 84550,
      currency = RUB
  )
  @Test
  @Description("Проверка возможности редактирования трат")
  void categoryDescriptionShouldBeEditedByTableAction(SpendJson spend) {
    final String newSpendingName = "Обучение Niffler Next Generation";

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .doLogin("max", "max")
        .editSpending(spend.description())
        .editSpendingDescription(newSpendingName);

    new MainPage().checkThatTableContains(newSpendingName);
  }
}