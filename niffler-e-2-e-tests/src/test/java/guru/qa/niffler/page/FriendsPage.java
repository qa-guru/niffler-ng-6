package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class FriendsPage {

    private final SelenideElement friendsList = $(".MuiButtonBase-root[href*='friends']");
    private final SelenideElement allPeoplesList = $(".MuiButtonBase-root[href*='all']");
    private final ElementsCollection allPeoplesTable = $("#all").$$("tr");
    private final ElementsCollection friendsTable = $("#friends").$$("tr");
    private final ElementsCollection friendRequestsTable = $("#requests").$$("tr");
    private final SelenideElement notFriendsText = $(By.xpath("//p[contains(text(), 'There are no users yet')]"));
    private final SelenideElement outcomingFriendRequestText = $(By.xpath("//span[contains(text(), 'Waiting...')]"));

    public FriendsPage clickShowFriendsList() {
        friendsList.click();
        return this;
    }

    public FriendsPage clickShowAllPeopleList() {
        allPeoplesList.click();
        return this;
    }

    public FriendsPage checkUserNotHaveFriend() {
        notFriendsText.should(visible);
        return this;
    }

    public FriendsPage checkUserPresentInFriendTable(String friendName) {
        friendsTable.find(text(friendName)).should(visible);
        return this;
    }

    public FriendsPage checkInvitationInFriendFromUserByName(String username) {
        friendRequestsTable.find(text(username)).should(visible);
        return this;
    }

    public FriendsPage checkOutgoingFriendInvitationRequestForUserByName(String username) {
        allPeoplesTable.find(text(username))
                .shouldHave(text("Waiting..."))
                .should(visible);
        return this;
    }
}
