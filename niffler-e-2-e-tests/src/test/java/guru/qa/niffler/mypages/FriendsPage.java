package guru.qa.niffler.mypages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.collections.SizeLessThanOrEqual;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

public class FriendsPage {
    private final SelenideElement friendsHeader = $x("//*[text()='Friends']");
    private final SelenideElement allPeople = $x("//*[text()='All people']");
    private final SelenideElement friendsTableHeader = $x("//*[text()='My friends']");
    private final SelenideElement friendRequestsTableHeader = $x("//*[text()='Friend requests']");
    private final String friendsTableBase = "//tbody[@id='friends']";
    private final String incomeRequestsTableBase = "//tbody[@id='requests']";
    private final String outcomeRequestsTableBase = "//tbody[@id='all']";
    private final String namePart = "//tr/td//div[contains(@class,'MuiBox-root')]/p[contains(@class,'MuiTypography-body1')]";
    private final ElementsCollection friendNames = $$x(friendsTableBase + namePart);
    private final ElementsCollection requestNames = $$x(incomeRequestsTableBase + namePart);
    private final ElementsCollection outcomeRequestNames = $$x(outcomeRequestsTableBase + namePart);

    public FriendsPage checkFriendsPage() {
        friendsHeader.shouldBe(visible);
        allPeople.shouldBe(visible);
        return this;
    }

    public void checkFriendNameInFriends(String friendName) {
        friendsTableHeader.shouldBe(visible);
        friendNames.stream()
                .filter(fn -> fn.getText().equals(friendName))
                .findFirst()
                .get()
                .shouldBe(visible);
    }

    public void checkFriendsTableIsEmpty() {
        friendNames.shouldBe(new SizeLessThanOrEqual(0));
    }

    public void checkFriendIncomeRequestExistInTable(String personName) {
        friendRequestsTableHeader.shouldBe(visible);
        requestNames.stream()
                .filter(fn -> fn.getText().equals(personName))
                .findFirst()
                .get()
                .shouldBe(visible);
    }

    public void selectAllPeople() {
        allPeople.shouldBe(visible)
                .click();
    }

    public void selectFriends() {
        friendsHeader.shouldBe(visible)
                .click();
    }

    public void checkFriendOutcomeRequestExistInTable(String personName) {
        outcomeRequestNames.stream()
                .filter(fn -> fn.getText().equals(personName))
                .findFirst()
                .get()
                .shouldBe(visible);
    }
}
