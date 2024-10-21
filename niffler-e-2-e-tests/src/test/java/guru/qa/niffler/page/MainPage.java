package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class MainPage {
    private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
    private final SelenideElement statisticsHeader = $x("//h2[text()='Statistics']");
    private final SelenideElement historyHeader = $x("//h2[text()='History of Spendings']");
    private final SelenideElement menuButton = $("[aria-label=Menu]");
    private final SelenideElement profileButton = $x("//a[@href='/profile']");
    private final SelenideElement friendButton = $x("(//a[@class='link nav-link'])[2]");
    private final SelenideElement allPeopleButton = $x("(//a[@class='link nav-link'])[3]");
    private final SelenideElement statComponent = $("#stat");
    private final SelenideElement spendingTable = $("#spendings");
    private final SelenideElement searchInput = $("input[type='text']");
    public EditSpendingPage editSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).$$("td").get(5).click();
        return new EditSpendingPage();
    }

    public void checkThatTableContainsSpending(String spendingDescription) {
        searchSpend(spendingDescription);
        tableRows.find(text(spendingDescription)).shouldBe(visible);
    }

    public MainPage shouldStatisticsHeader(String value) {
        statisticsHeader.shouldHave(text(value));
        return this;
    }

    public MainPage shouldHistoryHeader(String value) {
        historyHeader.shouldHave(text(value));
        return this;
    }

    public MainPage clickProfileButton() {
        menuButton.click();
        profileButton.click();
        return this;
    }

    public MainPage clickFriendButton() {
        menuButton.click();
        friendButton.click();
        return this;
    }

    public MainPage clickAllPeopleButton() {
        menuButton.click();
        allPeopleButton.click();
        return this;
    }
    public MainPage checkThatPageLoaded() {
        statComponent.should(visible).shouldHave(text("Statistics"));
        spendingTable.should(visible).shouldHave(text("History of Spendings"));
        return this;
    }

    public MainPage searchSpend(String spendingName) {
        searchInput.sendKeys(spendingName);
        searchInput.sendKeys(Keys.ENTER);
        return new MainPage();
    }

}
