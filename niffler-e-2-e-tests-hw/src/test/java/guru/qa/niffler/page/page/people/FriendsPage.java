package guru.qa.niffler.page.page.people;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static guru.qa.niffler.conditions.SelenideCondition.child;

@Slf4j
@NoArgsConstructor
@ParametersAreNonnullByDefault
public class FriendsPage extends PeoplePage<FriendsPage> {

    public FriendsPage(boolean checkPageElementVisible) {
        super(checkPageElementVisible);
    }

    private final SelenideElement friendRequestsListTitle = $(byText("Friend requests")).as("[Friend requests title]"),
            friendsListTitle = $(byText("My friends")).as("[Friends list title]"),
            friendRequestsTableContainer = $("#requests").as("[Friend requests table container]"),
            friendsTableContainer = $("#friends").as("[Friends list title]"),
            emptyFriendsPageTitle = $x("//p[text()='There are no users yet']").as("[Empty friends page title]"),
            emptyFriendsPageImage = $x("//img[@alt='Lonely niffler']").as("[Empty friends page title]");

    private final ElementsCollection friendRequestsList = friendRequestsTableContainer.$$("tr").as("'Friend requests' list"),
            friendsList = friendsTableContainer.$$("tr").as("'Friends' list");

    @Step("Switch to 'All people' page")
    public AllPeoplePage switchToAllPeopleTab() {
        log.info("Switching to 'All people' tab");
        allPeopleTab.click();
        return new AllPeoplePage(true);
    }

    @Step("Unfriend user = [{username}]")
    public FriendsPage unfriend(String username) {
        log.info("Unfriend user = [{}]", username);
        filterByQuery(username);
        friendsList.filterBy(child(byXpath(".//p[1]"), exactText(username))).get(0)
                .$x(".//button[text()='Unfriend']").as("['Unfriend' " + username + " button]").click();
        return this;
    }

    @Step("Accept income friend request from user = [{username}]")
    public FriendsPage acceptFriendRequest(String username) {
        log.info("Accept friend request from user = [{}]", username);
        filterByQuery(username);
        friendRequestsList.filterBy(child(byXpath(".//p[1]"), exactText(username))).get(0)
                .$x(".//button[text()='Accept']").as("['Accept' " + username + " request button]").click();
        return this;
    }

    @Step("Decline income friend request from user = [{username}]")
    public FriendsPage declineFriendRequest(String username) {
        log.info("Decline friend request from user = [{}]", username);
        filterByQuery(username);
        friendRequestsList.filterBy(child(byXpath(".//p[1]"), exactText(username))).get(0)
                .$x(".//button[text()='Decline']").as("['Decline' " + username + " request button]").click();
        return this;
    }

    @Step("Should have income friend request from = [{username}]")
    public FriendsPage shouldHaveIncomeFriendRequest(String username) {
        log.info("Assert friend request exists with username = [{}]", username);
        filterByQuery(username);
        friendRequestsList.filterBy(child(usernameSelector, exactText(username))).get(0).shouldBe(visible);
        return this;
    }

    @Step("Should have friend = [{username}]")
    public FriendsPage shouldHaveFriend(String username) {
        log.info("Assert friend exists with username = [{}]", username);
        filterByQuery(username);
        friendsList.filterBy(child(usernameSelector, exactText(username))).get(0).shouldBe(visible);
        return this;
    }

    @Step("Should not have friend = [{username}]")
    public FriendsPage shouldNotHaveFriend(String username) {
        log.info("Assert friend exists with username = [{}]", username);
        filterByQuery(username);
        friendsList.filterBy(child(usernameSelector, exactText(username))).shouldHave(size(0));
        return this;
    }

    @Step("Should empty friends page")
    public FriendsPage shouldBeEmptyFriendsPage() {

        log.info("Assert friend requests and friends list are empty");

        friendRequestsListTitle.shouldNot(exist);
        friendRequestsTableContainer.shouldNot(exist);
        friendsListTitle.shouldNot(exist);
        friendsTableContainer.shouldNot(exist);

        emptyFriendsPageTitle.shouldBe(visible);
        emptyFriendsPageImage.shouldBe(visible);

        return this;

    }

    @Override
    public FriendsPage shouldVisiblePageElement() {
        log.info("Assert 'Friends' page element visible on start up");
        friendsTab.shouldHave(attribute("aria-selected", "true"));
        return this;
    }

    @Override
    @Step("Should visible 'Friends' page")
    public FriendsPage shouldVisiblePageElements() {
        log.info("Assert 'Friends' page elements visible on start up");
        friendsTab.shouldHave(attribute("aria-selected", "true"));
        return this;
    }
}
