package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class PeoplePage {

    private static final String ACCEPT_BUTTON_TEXT = "Accept";
    private static final String DECLINE_BUTTON_TEXT = "Decline";
    private static final String UNFRIEND_BUTTON_TEXT = "Unfriend";
    private static final String WAITING_LABEL_TEXT = "Waiting...";

    private final SelenideElement searchInput = $("input[placeholder='Search']");
    private final SelenideElement searchButton = $("button[aria-label='search']");
    private final SelenideElement friendsTab = $("a[href='/people/friends']");
    private final SelenideElement allPeopleTab = $("a[href='/people/all']");
    private final SelenideElement myFriendsHeader = $$(".MuiTableContainer-root h2.MuiTypography-root").findBy(exactText("My friends"));
    private final SelenideElement friendRequestsHeader = $$(".MuiTableContainer-root h2.MuiTypography-root").findBy(exactText("Friend requests"));
    private final SelenideElement previousPageButton = $("#page-prev");
    private final SelenideElement nextPageButton = $("#page-next");
    private final ElementsCollection friendsList = $$("#friends .MuiTableRow-root");
    private final ElementsCollection friendRequestsList = $$("#requests .MuiTableRow-root");
    private final SelenideElement friendsListTable = $("#friends");
    private final SelenideElement friendRequestsListTable = $("#requests");
    private final SelenideElement noUsersText = $$("p.MuiTypography-root").findBy(exactText("There are no users yet"));
    private final ElementsCollection allPeopleList = $$("#all .MuiTableRow-root");
    private final SelenideElement waitingLabel = $("span.MuiChip-label:contains('Waiting...')");
    private final SelenideElement loadingSpinner = $("div.MuiCircularProgress-root");

    public PeoplePage checkThatFriendPresentInFriendsList(String name) {
        loadingSpinner.shouldNotBe(visible);
        friendsList.stream()
                .filter(r -> r.find(By.cssSelector("p"))
                        .getText()
                        .equals(name))
                .findFirst()
                .ifPresentOrElse(
                        r -> {
                            r.$("p.MuiTypography-root.MuiTypography-body1")
                                    .shouldBe(visible)
                                    .shouldHave(exactText(name));
                            r.$$("button").findBy(exactText(UNFRIEND_BUTTON_TEXT))
                                    .shouldBe(visible)
                                    .shouldBe(enabled)
                                    .shouldBe(enabled);
                        },
                        () -> {
                            throw new AssertionError("Friend not found: " + name);
                        }
                );
        return this;
    }

    public PeoplePage checkThatFriendNotPresentInFriendsList(String name) {
        friendsList.find(Condition.text(name))
                .shouldNotBe(Condition.visible);
        return this;
    }

    public PeoplePage checkThatFriendsListIsEmpty() {
        friendsListTable.shouldNot(Condition.exist);
        noUsersText.shouldBe(Condition.visible);
        return this;
    }

    public PeoplePage checkThatIncomingRequestPresentInFriendsList(String user) {

        // The only solution I was able to find
        // that works with STRICT search by username.
        // friendRequestsList.find(exactText(name)) doesn't work for me
        friendRequestsList.stream()
                .filter(r -> r.find(By.cssSelector("p"))
                        .getText()
                        .equals(user))
                .findFirst()
                .ifPresentOrElse(
                        r -> {
                            r.$("p.MuiTypography-root.MuiTypography-body1").shouldBe(visible)
                                    .shouldHave(exactText(user));
                            r.$$("button").findBy(exactText(ACCEPT_BUTTON_TEXT))
                                    .shouldBe(visible)
                                    .shouldBe(enabled);
                            r.$$("button").findBy(exactText(DECLINE_BUTTON_TEXT))
                                    .shouldBe(visible)
                                    .shouldBe(enabled);
                        },
                        () -> {
                            throw new AssertionError("Incoming request not found for user: " + user);
                        }
                );
        return this;
    }

    public PeoplePage clickAllPeopleTab() {
        allPeopleTab.click();
        loadingSpinner.shouldNotBe(visible);
        return this;
    }

    public void checkThatOutgoingRequestPresentInAllPeoplesList(String user) {
        allPeopleList.stream()
                .filter(r -> r.find(By.cssSelector("p"))
                        .getText()
                        .equals(user))
                .findFirst()
                .ifPresentOrElse(
                        r -> {
                            r.$("p.MuiTypography-root.MuiTypography-body1")
                                    .shouldBe(visible)
                                    .shouldHave(exactText(user));
                            r.$$("span.MuiChip-label").findBy(text(WAITING_LABEL_TEXT))
                                    .shouldBe(visible);
                        },
                        () -> {
                            throw new AssertionError("Outgoing request not found for user: " + user);
                        }
                );
    }
}