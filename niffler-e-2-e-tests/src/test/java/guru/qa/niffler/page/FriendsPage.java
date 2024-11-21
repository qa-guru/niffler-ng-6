package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FriendsPage {

    private final SelenideElement allPeopleSection = $x(".//h2[text()='All people']");
    private final ElementsCollection friendsTableRows = $$("#friends");

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
}
