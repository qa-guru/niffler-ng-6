package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class AllPeoplePage {
    private final SelenideElement friendsPage = $("a[href='/people/friends']");
    private final ElementsCollection listAllPeople = $$("#all tr");

    public AllPeoplePage openAllPeoplePage() {
        friendsPage.click();
        return new AllPeoplePage();
    }

    public void checkHaveOutcomeInvitation(String friendName) {
        listAllPeople.find(text(friendName)).should(visible);
        listAllPeople.find(text("Waiting...")).should(visible);
    }

    public void checkNotHaveOutcomeInvitation() {
        listAllPeople.find(text("Waiting...")).should(disappear);
    }
}