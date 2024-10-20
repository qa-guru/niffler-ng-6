package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class AllPeoplePage {
    private final SelenideElement bottonToOpenFriendsPage = $("a[href='/people/friends']");
    private final ElementsCollection listAllPeople = $$("#all tr");
    private final SelenideElement fieldSearch = $("input[placeholder='Search']");
    private final SelenideElement buttonSearch = $("button[id='input-submit']");

    public FriendsPage openFriendsPeoplePage() {
        bottonToOpenFriendsPage.click();
        return new FriendsPage();
    }

    public AllPeoplePage toSearch(String searchString){
        fieldSearch.setValue(searchString);
        buttonSearch.click();
        return new AllPeoplePage();
    }

    public void checkHaveOutcomeInvitation() {
        listAllPeople.find(text("Waiting...")).should(visible);
    }

    public void checkNotHaveOutcomeInvitation() {
        listAllPeople.find(text("Waiting...")).should(disappear);
    }
}