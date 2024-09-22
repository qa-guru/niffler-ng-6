package guru.qa.nifler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.nifler.config.Config;
import guru.qa.nifler.jupiter.Spend;
import guru.qa.nifler.page.LoginPage;
import guru.qa.nifler.page.MainPage;
import org.junit.jupiter.api.Test;

import static guru.qa.nifler.enums.CurrencyValues.RUB;

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
  void categoryDescriptionShouldBeEditedByTableAction() {
    final String newSpendingName = "Обучение Niffler Next Generation";

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .doLogin("max", "max")
        .editSpending("Обучение")
        .editSpendingDescription(newSpendingName);

    new MainPage().checkThatTableContains(newSpendingName);
  }
}