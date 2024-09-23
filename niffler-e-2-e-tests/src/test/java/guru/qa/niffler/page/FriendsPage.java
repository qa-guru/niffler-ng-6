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


    public AllPeoplePage openAllPeoplePage() {
        buttonToOpenAllPeoplePage.click();
        return new AllPeoplePage();
    }

    public void checkHaveFriend(String friendName) {
        listFriends.find(text(friendName)).should(visible);
    }

    public void checkIncomeInvitationFriend(String friendName) {
        listRequests.find(text(friendName)).$$("td").find(text("Accept")).should(visible);
    }

    public void checkNotHaveFriend() {
        listFriends.shouldHave(size(0));
    }
}