package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class FriendsPage {
    private static final ElementsCollection friendsTable = $$("#friends tr");
    private static final SelenideElement emptyTableText = $(".css-1m7obeg");
    private static final ElementsCollection friendsRequestsTable = $$("#requests tr");
    private static final ElementsCollection allPeopleTable = $$("#all tr");

    public FriendsPage friendShouldBeInTable(String username) {
        friendsTable.findBy(text(username)).shouldBe(visible);
        return this;
    }

    public FriendsPage checkEmptyFriendsTable(String message) {
        emptyTableText.shouldHave(text(message));
        return this;
    }

    public FriendsPage requestShouldBeInTable(String username) {
        friendsRequestsTable.findBy(text(username)).shouldBe(visible);
        return this;
    }

    public FriendsPage outcomeInvitationShouldBeInTheTable(String username, String waitingText) {
        allPeopleTable
                .findBy(text(username))
                .shouldBe(visible)
                .$(".css-bvvzrr")
                .shouldHave(text(waitingText));

        return this;
    }
}
