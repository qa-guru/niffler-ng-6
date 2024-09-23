package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class AllPeoplePage {
    private final SelenideElement bottonToOpenFriendsPage = $("a[href='/people/friends']");
    private final ElementsCollection listAllPeople = $$("#all tr");

    public FriendsPage openFriendsPeoplePage() {
        bottonToOpenFriendsPage.click();
        return new FriendsPage();
    }

    public void checkHaveOutcomeInvitation(String friendName) {
        listAllPeople.find(text(friendName)).$$("td").find(text("Waiting...")).should(visible);
    }

    public void checkNotHaveOutcomeInvitation() {
        listAllPeople.find(text("Waiting...")).should(disappear);
    }
}