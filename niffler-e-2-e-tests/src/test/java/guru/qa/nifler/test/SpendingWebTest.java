package guru.qa.nifler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.nifler.config.Config;
import guru.qa.nifler.jupiter.BrowserExtension;
import guru.qa.nifler.jupiter.Spend;
import guru.qa.nifler.model.SpendJson;
import guru.qa.nifler.page.LoginPage;
import guru.qa.nifler.page.MainPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.nifler.enums.CurrencyValues.RUB;

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
  void categoryDescriptionShouldBeEditedByTableAction(SpendJson spend) {
    final String newSpendingName = "Обучение Niffler Next Generation";

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .doLogin("max", "max")
        .editSpending(spend.description())
        .editSpendingDescription(newSpendingName);

    new MainPage().checkThatTableContains(newSpendingName);
  }
}