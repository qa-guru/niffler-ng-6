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

  private final SelenideElement profileTabsButton = $("button[aria-label='Menu']").as("Иконка профиля с выпадающим меню");
  private final SelenideElement profileTab = $x("//ul/li/a[@href='/profile']").as("Вкладка 'Profile' с выпадающим меню");
  private final SelenideElement friendsTab = $x("//ul/li/a[@href='/people/friends']").as("Вкладка 'Friends' с выпадающим меню");
  private final SelenideElement allPeopleTab = $x("//ul/li/a[@href='/people/all']").as("Вкладка 'All People' с выпадающим меню");
  private final SelenideElement signOutButton = $x("//ul/li[text()='Sign out']").as("Кнопка 'Sign out' с выпадающим меню");


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

  @Step("Перейти на страницу 'Friends'")
  public FriendsPage goToFriendsTab() {
    profileTabsButton.click();
    friendsTab.click();
    return new FriendsPage();
  }

  @Step("Перейти на страницу 'All People'")
  public AllPeoplePage goToAllPeopleTab() {
    profileTabsButton.click();
    allPeopleTab.click();
    return new AllPeoplePage();
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