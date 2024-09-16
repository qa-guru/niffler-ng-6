package guru.qa.niffler.page;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class FriendsPage {
    private final ElementsCollection friendList = $$("tbody#friends tr");
    private final ElementsCollection friendReqList = $$("#requests tr");
    private final ElementsCollection allPeopleList = $$("tbody#all tr");

    private final SelenideElement allPeopleButton = $("[aria-label='People tabs'] [href='/people/all']");
    private final SelenideElement emptyFriendListText = $x("//p[text()='There are no users yet']");
    private final SelenideElement myFriendsHeader = $x("//h2[text()='My friends']");
    private final SelenideElement friendRequestsHeader = $x("//h2[text()='Friend requests']");

    public FriendsPage checkNameInFriendList(String name) {
        friendList.findBy(text(name)).shouldBe(visible);
        return this;
    }

    public FriendsPage checkNameInRequestList(String name) {
        friendReqList.findBy(text(name)).shouldBe(visible);
        return this;
    }

    public FriendsPage checkNameInAllPeopleList(String name) {
        allPeopleList
                .findBy(text(name)).shouldBe(visible);
        return this;
    }

    public FriendsPage checkOutcomeInvitationInAllPeopleList(String name) {
        allPeopleList.findBy(text(name))
                .shouldHave(text("Waiting..."));
        return this;
    }

    public FriendsPage friendListShouldBeEmpty() {
        emptyFriendListText.shouldBe(visible);
        friendList.shouldBe(CollectionCondition.empty);
        return this;
    }

    public FriendsPage myFriendsHeaderShouldBeVisible() {
        myFriendsHeader.shouldBe(visible);
        return this;
    }

    public FriendsPage myFriendsHeaderShouldNotBeVisible() {
        myFriendsHeader.shouldNotBe(visible);
        return this;
    }

    public FriendsPage friendRequestsHeaderShouldBeVisible() {
        friendRequestsHeader.shouldBe(visible);
        return this;
    }

    public FriendsPage friendRequestsHeaderShouldNotBeVisible() {
        friendRequestsHeader.shouldNotBe(visible);
        return this;
    }

    public FriendsPage clickAllPeople() {
        allPeopleButton.click();
        return this;
    }
}
