package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.util.List;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.openqa.selenium.By.xpath;

public class AllPeoplePage extends PeoplePage {

  @Step("Проверка исходящего запроса в друзья")
  public AllPeoplePage checkOutcomeFriendsRequest(List<String> usersNameRequest) {
    usersNameRequest.forEach(uNR -> {
      searchInField(uNR);
      SelenideElement friendsOutcomeRequest = $x("//tbody//tr//p[text()='" + uNR + "']/ancestor::tr")
          .as("Строка исходящего запроса в друзья");
      friendsOutcomeRequest.shouldBe(visible);
      friendsOutcomeRequest.find(xpath(".//span[text()='Waiting...']"))
          .as("Значок ожидания('Waiting...') принятия запроса").shouldBe(visible);
    });
    return this;
  }
}
