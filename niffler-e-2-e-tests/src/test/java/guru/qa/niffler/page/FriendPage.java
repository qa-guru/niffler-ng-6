package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class FriendPage {

    private final SelenideElement friendsButton = $("div>a[href='/people/friends']");
    private final SelenideElement allPeopleButton = $("div>a[href='/people/all']");
    private final SelenideElement emptyFriendPanelText = $("#simple-tabpanel-friends [class^='MuiTypography-root']");
    private final ElementsCollection friendsList = $$("#friends>tr p:nth-child(1)");
    private final ElementsCollection friendNameInRequestsList = $$("#requests tr td:nth-child(1) p:nth-child(1)");
    private final ElementsCollection allPeopleList = $$("#all tr");

    public FriendPage clickOnFriendButton() {
        friendsButton.click();
        return this;
    }

    public FriendPage clickOnAllPeopleButton() {
        allPeopleButton.click();
        return this;
    }

    public void checkIfNoFriendsInPanel(String message) {
        emptyFriendPanelText.shouldHave(text(message));
    }

    public void checkIfFriendNameIsVisible(String friendName) {
        friendsList.findBy(text(friendName)).shouldBe(visible);
    }

    public void checkIfFriendNameIsVisibleInRequestsList(String friendName) {
        friendNameInRequestsList.findBy(text(friendName)).shouldBe(visible);
    }

    public void checkIfOutcomeFriendRequestIsVisibleInAllList(String friendName) {
        allPeopleList.findBy(text(friendName)).shouldBe(visible);
        allPeopleList.findBy(text(friendName)).shouldHave(text("Waiting..."));
    }
}
