package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ElementsCollection;
import guru.qa.niffler.page.component.FriendsRequestTable;
import guru.qa.niffler.page.component.SearchField;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class FriendsPage {
    private final SelenideElement buttonToOpenAllPeoplePage = $("a[href='/people/all']");
    private final ElementsCollection listFriends = $$("#friends tr");
    private final SearchField searchField = new SearchField($("input[aria-label='search']"));
    private final FriendsRequestTable friendsRequestTable = new FriendsRequestTable();


    public AllPeoplePage openAllPeoplePage() {
        buttonToOpenAllPeoplePage.click();
        return new AllPeoplePage();
    }

    public void checkHaveFriend() {
        listFriends.find(text("Unfriend")).should(visible);
    }

    public void checkIncomeInvitationFriend(String searchString) {
        searchField.search(searchString);
        listFriends.find(text("Accept")).should(visible);
    }

    public void checkNotHaveFriend() {
        listFriends.shouldHave(size(0));
    }

    public void acceptFriendship(String username) {
        friendsRequestTable.acceptFriendRequest(username);
    }

    public void declineFriendship(String username) {
        friendsRequestTable.declineFriendRequest(username);
    }
}