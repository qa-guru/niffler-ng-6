package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class MainPage {
    private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
    private final SelenideElement spendingHistoryTable = $("#spendings");
    private final SelenideElement spendingStatisticsCanvas = $("#stat");
    private final SelenideElement statisticsHeader = $(".css-giaux5");
    private final SelenideElement historySpendingHeader = $(".css-uxhuts");
    private final SelenideElement menuButton = $(".css-1obba8g");
    private final SelenideElement profileButton = $(".nav-link[href*='profile']");
    private final SelenideElement friendsButton = $(".nav-link[href*='friends']");
    private final ElementsCollection categoryRows = $$(".css-gq8o4k");


    public EditSpendingPage editSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).$$("td").get(5).click();
        return new EditSpendingPage();
    }

    public MainPage clickAvatarButton() {
        menuButton.click();
        return this;
    }

    public ProfilePage clickProfileButton() {
        profileButton.click();
        return new ProfilePage();
    }

    public FriendsPage clickFriendsButton() {
        friendsButton.click();
        return new FriendsPage();
    }

    public void checkThatTableContainsSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).should(visible);
    }

    public MainPage checkThatNameOfStatisticsHeaderIsDisplayed(String headerText) {
        statisticsHeader.shouldHave(text(headerText)).shouldBe(visible);
        return this;
    }

    public MainPage checkThatNameOfHistorySpendingHeaderIsDisplayed(String headerText) {
        historySpendingHeader.shouldHave(text(headerText)).shouldBe(visible);
        return this;
    }
}
