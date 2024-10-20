package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ElementsCollection;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class FriendsPage {
    private final SelenideElement buttonToOpenAllPeoplePage = $("a[href='/people/all']");
    private final ElementsCollection listFriends = $$("#friends tr");
    private final ElementsCollection listRequests = $$("#requests tr");
    private final SelenideElement fieldSearch = $("input[placeholder='Search']");
    private final SelenideElement buttonSearch = $("button[id='input-submit']");



    public AllPeoplePage openAllPeoplePage() {
        buttonToOpenAllPeoplePage.click();
        return new AllPeoplePage();
    }

    public FriendsPage toSearch(String searchString){
        fieldSearch.setValue(searchString);
        buttonSearch.click();
        return new FriendsPage();
    }

    public void checkHaveFriend() {
        listFriends.find(text("Unfriend")).should(visible);
    }

    public void checkIncomeInvitationFriend() {
        listFriends.find(text("Accept")).should(visible);
    }

    public void checkNotHaveFriend() {
        listFriends.shouldHave(size(0));
    }
}