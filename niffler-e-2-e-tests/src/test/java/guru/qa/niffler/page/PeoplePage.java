package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.SearchField;
import io.qameta.allure.Step;

import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class PeoplePage {
    private final SelenideElement peopleTable = $("#all");
    private final SearchField searchField = new SearchField();

    @Step("Проверка отправки приглашения в друзья")
    public PeoplePage checkInvitationSentToUser(List<String> usernames) {
        for (String username : usernames) {
            searchField.search(username);
            SelenideElement friendRow = peopleTable.$$("tr").find(text(username));
            friendRow.shouldHave(text("Waiting..."));
        }
        return this;
    }
}
