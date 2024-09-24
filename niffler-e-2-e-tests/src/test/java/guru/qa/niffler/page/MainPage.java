package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class MainPage {

  private final ElementsCollection tableRows = $$("#spendings tbody tr").as("Таблица трат");
  private final SelenideElement statHeaderText = $("div[id='stat'] h2").as("Тест заголовка 'Statistics'");

  @Step("Перейти к редактированию траты = {spendingDescription}")
  public EditSpendingPage editSpending(String spendingDescription) {
    tableRows.find(text(spendingDescription)).$("td button").click();
    return new EditSpendingPage();
  }

  @Step("Проверка наличие траты = {spendingDescription} в таблице")
  public void checkThatTableContains(String spendingDescription) {
    tableRows.find(text(spendingDescription)).should(visible);
  }

  @Step("Проверка отображения текст главного экрана трат")
  public void checkSuccessLogin() {
    statHeaderText.should(visible);
  }
}