package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
    private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
    private final SelenideElement menuButton = $("button[aria-label='Menu']");
    private final SelenideElement profileLink = $("a[href='/profile']");
    private final SelenideElement friendsLink = $("a[href='/people/friends']");
    private final SelenideElement allPeopleLink = $("a[href='/people/all']");
    private final SelenideElement statBlock = $("#stat");
    private final SelenideElement fieldSearch = $("input[placeholder='Search']");
    private final SelenideElement buttonSearch = $("button[id='input-submit']");

    public EditSpendingPage editSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).$$("td").get(5).click();
        return new EditSpendingPage();
    }

    public MainPage toSearch(String searchString){
        fieldSearch.setValue(searchString);
        buttonSearch.click();
        return new MainPage();
    }

    public void checkThatTableContainsSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).should(visible);
    }

    public ProfilePage openProfilePage() {
        menuButton.click();
        profileLink.click();
        return new ProfilePage();
    }

    public FriendsPage openFriendsPage() {
        menuButton.click();
        friendsLink.click();
        return new FriendsPage();
    }

    public AllPeoplePage openAllPeoplePage() {
        menuButton.click();
        allPeopleLink.click();
        return new AllPeoplePage();
    }

    public void checkStatisticBlock() {
        statBlock.should(visible);
    }

}