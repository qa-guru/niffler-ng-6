package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.SearchField;
import io.qameta.allure.Step;
import lombok.Getter;

import java.util.List;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class FriendsPage extends BasePage<FriendsPage> {
    private final SelenideElement requestsTable = $("#requests");
    private final SelenideElement friendsTable = $("#friends");
    private final SelenideElement acceptButton = $(byText("Accept"));
    private final SelenideElement declineButton = $(byText("Decline"));
    private final SelenideElement confirmDeclineButton =
            $(".MuiPaper-root button.MuiButtonBase-root.MuiButton-containedPrimary");

    @Getter
    private final SearchField searchField = new SearchField<>($("input[type='text']"), this);

    @Step("Проверка наличия имен в списке друзей")
    public FriendsPage checkExistingFriends(List<String> expectedUsernames) {
        for (String expectedUsername : expectedUsernames) {
            searchField.search(expectedUsername);
            friendsTable.$$("tr").find(text(expectedUsername)).should(visible);
        }
        return this;
    }

    @Step("Проверка отсутствия имен в списке друзей")
    public FriendsPage checkNoExistingFriends() {
        friendsTable.$$("tr").shouldHave(size(0));
        return this;
    }

    @Step("Проверка наличия приглашения")
    public FriendsPage checkExistingInvitations(List<String> expectedUsernames) {
        for (String expectedUsername : expectedUsernames) {
            searchField.search(expectedUsername);
            requestsTable.$$("tr").find(text(expectedUsername)).should(visible);
        }
        return this;
    }

    @Step("Принять заявку в друзья")
    public FriendsPage acceptFriend() {
        acceptButton.click();
        return this;
    }

    @Step("Отклонить заявку в друзья")
    public FriendsPage declineFriend() {
        declineButton.click();
        confirmDeclineButton.click();
        return this;
    }

}
