package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FriendsPage {

    private final SelenideElement allPeopleSection = $x(".//h2[text()='All people']");
    private final ElementsCollection friendsTableRows = $$("#friends");
    private final SelenideElement thereAreNoUsersYet = $x(".//p[text()='There are no users yet']");
    private final String acceptButton = ".//button[text()='Accept']";
    private final String waitingMessage = ".//span[text()='Waiting...']";

    @Step("Open all people section")
    public void openAllPeopleSection() {
        allPeopleSection.click();
    }

    @Step("Check that friend present in friends table")
    public void checkThatFriendPresentInFriendsTable(String friend) {
        friendsTableRows.findBy(text(friend)).shouldBe(visible);
        boolean isFriendPresent = friendsTableRows.findBy(text(friend)).is(visible);
        assertTrue(isFriendPresent, friend + " is not found in friends table");
    }

    @Step("Check that friends table is empty")
    public void checkThatFriendsTableIsEmpty() {
        thereAreNoUsersYet.shouldBe(visible);
    }

    public ElementsCollection findFriend(String friend) {
        ElementsCollection elements = $$(".MuiTableRow-root").filterBy(Condition.text(friend));
        elements.shouldBe(sizeGreaterThan(0), Duration.ofSeconds(5));
        return elements;
    }

    @Step("Check that income invitation in friends table")
    public void checkThatIncomeInvitationInFriendsTable(String friendIncome) {
        ElementsCollection elements = findFriend(friendIncome);
        assertTrue(elements.first().$x(acceptButton).is(visible), friendIncome + " not founded in Friend requests");
    }

    @Step("Check that income invitation in friends table")
    public void checkThatOutcomeInvitationInFriendsTable(String friendOutcome) {
        ElementsCollection elements = findFriend(friendOutcome);
        assertTrue(elements.first().$x(waitingMessage).is(visible), friendOutcome + " not founded in Friend outcome");
    }
}
