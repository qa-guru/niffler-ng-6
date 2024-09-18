package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class FriendsPage {

    private final ElementsCollection friendsRows = $$("#friends tr");
    private final ElementsCollection requestsRows = $$("#requests tr");
    private final ElementsCollection allPeopleRows = $$("#all tr");
    private final SelenideElement panelFriends = $("#simple-tabpanel-friends");

    public void shouldBePresentInFriendsTable(String friendName) {
        friendsRows.findBy(text(friendName)).shouldBe(visible);
    }

    public void shouldHaveEmptyFriendsTable(String emptyMessage) {
        panelFriends.shouldHave(text(emptyMessage));
    }

    public void shouldBePresentInRequestsTable(String friendName) {
        requestsRows.findBy(text(friendName)).shouldBe(visible);
    }

    public void outcomeInvitationBePresentInAllPeoplesTable(String friendName) {
        allPeopleRows.findBy(text(friendName)).shouldBe(visible);
        allPeopleRows.find(text(friendName)).shouldHave(text("Waiting..."));
    }
}