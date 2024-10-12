package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$x;
import static org.junit.jupiter.api.Assertions.assertAll;

public class MainPage {

  private final ElementsCollection tableRows = $$("#spendings tbody tr").as("Таблица трат");
  private final SelenideElement statHeaderText = $("div[id='stat'] h2").as("Текст заголовка 'Statistics'");
  private final SelenideElement historyOfSpendingsHeaderText = $("div[id='spendings'] h2").as("Текст заголовка 'History of Spendings'");
  private final SelenideElement createNewSpendButton = $x("//a[contains(@class,'MuiButtonBase-root')]").as("Кнопка 'New spending'");
  private final SelenideElement loginErrorText = $("p[class='form__error']").as("Тест ошибки авторизации");

  @Step("Перейти к редактированию траты = {spendingDescription}")
  public EditSpendingPage editSpending(String spendingDescription) {
    tableRows.find(text(spendingDescription)).$("td button")
        .as("Кнопка траты " + spendingDescription).click();
    return new EditSpendingPage();
  }

  @Step("Проверка наличие траты = {spendingDescription} в таблице")
  public MainPage checkThatTableContains(String spendingDescription) {
    tableRows.find(text(spendingDescription)).should(visible);
    return this;
  }

  @Step("Проверка текста ошибки при Логине")
  public MainPage checkLoginErrorText(String errorText) {
    loginErrorText.shouldHave(text(errorText));
    return this;
  }

  @Step("Проверка отображения текст главного экрана трат")
  public MainPage checkSuccessLogin() {
    assertAll(
        () -> statHeaderText.should(visible),
        () -> historyOfSpendingsHeaderText.should(visible)
    );
    return this;
  }

  @Step("Перейти к созданию траты")
  public EditSpendingPage goToCreateNewSpendingPage() {
    createNewSpendButton.click();
    return new EditSpendingPage();
  }
}