package guru.qa.niffler.page.page.people;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.conditions.SelenideCondition.child;

@Slf4j
@NoArgsConstructor
@ParametersAreNonnullByDefault
public class AllPeoplePage extends PeoplePage<AllPeoplePage> {

    private final SelenideElement allPeopleTableContainer = $("#all").as("['All people' table]");
    private final ElementsCollection allPeopleList = allPeopleTableContainer.$$("tr").as("'All people' list");

    public AllPeoplePage(boolean checkPageElementVisible) {
        super(checkPageElementVisible);
    }

    @Step("Switch to 'Friends' tab")
    public FriendsPage switchToFriendsTab() {
        log.info("Switching to 'Friends' tab");
        friendsTab.shouldBe(clickable).click();
        return new FriendsPage(true);
    }

    @Step("Send friend request to user = [{}]")
    public AllPeoplePage sendFriendRequestToUser(String username) {
        log.info("Send friend request to user = [{}]", username);
        filterByQuery(username);
        allPeopleList.findBy(child(usernameSelector, exactText(username))).$(addFriendButtonSelector)
                .as("['Add friend' button of user [" + username + "]]").shouldBe(clickable).click();
        return this;
    }

    @Step("Should send friend request to user = [{}]")
    public AllPeoplePage shouldHaveOutcomeFriendRequest(String username) {
        log.info("Check user record [{}] have status waiting confirm", username);
        filterByQuery(username);
        allPeopleList.findBy(child(usernameSelector, exactText(username)))
                .shouldHave(child(waitingButtonSelector, exactText("Waiting...")));
        return this;
    }

    @Step("Should get friend request from user = [{}]")
    public AllPeoplePage shouldHaveStatusNonFriend(String username) {
        log.info("Check user record [{}] have status non-friend", username);
        filterByQuery(username);
        allPeopleList.findBy(child(usernameSelector, exactText(username)))
                .shouldHave(child(addFriendButtonSelector, exactText("Add friend")));
        return this;
    }

    @Override
    public AllPeoplePage shouldVisiblePageElement() {
        log.info("Assert 'All people' page element visible on start up");
        friendsTab.shouldHave(attribute("aria-selected", "true"));
        return this;
    }

    @Override
    @Step("Should visible 'All people' page")
    public AllPeoplePage shouldVisiblePageElements() {
        log.info("Assert 'All people' page element visible on start up");
        friendsTab.shouldHave(attribute("aria-selected", "true"));
        return this;
    }


}
