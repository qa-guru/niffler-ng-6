package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public abstract class PeoplePage {
  private final SelenideElement friendsTab = $("//div[@aria-label='People tabs']//h2[text()='Friends']").as("Вкладка 'Friends'");
  private final SelenideElement allPeopleTab = $("//div[@aria-label='People tabs']//h2[text()='All people']").as("Вкладка 'All people'");
  private final SelenideElement searchInput = $("input[aria-label='search']").as("Поисковое поле ввода");

  @Step("Переключиться на страницу 'Friends'")
  protected FriendsPage switchToFriendsPage() {
    friendsTab.click();
    return new FriendsPage();
  }

  @Step("Переключиться на страницу 'All people'")
  protected AllPeoplePage switchToAllPeoplePage() {
    allPeopleTab.click();
    return new AllPeoplePage();
  }

  @Step("Найти в списке")
  protected PeoplePage searchInField(String searchValue) {
    searchInput.setValue(searchValue).pressEnter();
    return this;
  }
}
