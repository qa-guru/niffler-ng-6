package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.openqa.selenium.By.xpath;

public class FriendsPage extends PeoplePage {
  private final SelenideElement friendsRequestTable = $("tbody[id='requests']").as("Таблица 'Friend requests'");
  private final SelenideElement myFriendsTable = $("tbody[id='friends']").as("Таблица 'My friends'");

  @Step("Проверка наличия друзей в списке")
  public FriendsPage checkFriendsIsExist(List<String> friendName) {
    friendName.forEach(fN -> {
      searchInField(fN);
      myFriendsTable.find(xpath(".//tr[//div/p[text()='" + fN + "']]")).as("Строка друга " + fN)
          .shouldBe(visible)
          .find(xpath(".//button")).as("Кнопка отмены дружбы").shouldHave(text("Unfriend"));
    });
    return this;
  }

  @Step("Проверка отсутствия друзей в списке")
  public FriendsPage checkFriendsIsNotExist() {
    myFriendsTable.find(xpath(".//tr[//div/p])"))
        .as("Строка друга").shouldNotBe(visible);
    return this;
  }

  @Step("Проверка входящего запроса в друзья")
  public FriendsPage checkIncomeFriendsRequest(List<String> friendsNameRequest) {
    friendsNameRequest.forEach(fNR -> {
      searchInField(fNR);
      SelenideElement friendsIncomeRequest = friendsRequestTable.find(xpath(".//tr[//div/p[text()='" + fNR + "']]"))
          .as("Строка входящего запроса в друзья");
      friendsIncomeRequest.shouldBe(visible);
      assertAll(
          () -> friendsIncomeRequest.find(xpath(".//button[text()='Accept']"))
              .as("Кнопка принятия('Accept') дружбы").shouldBe(visible),
          () -> friendsIncomeRequest.find(xpath(".//button[text()='Decline']"))
              .as("Кнопка принятия('Decline') дружбы").shouldBe(visible)
      );
    });
    return this;
  }
}
