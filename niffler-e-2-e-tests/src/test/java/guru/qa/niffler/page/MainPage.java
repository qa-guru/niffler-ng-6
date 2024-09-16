package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class MainPage {
    private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
    private final SelenideElement statisticsHeader = $x("//h2[text()='Statistics']");
    private final SelenideElement historyOfSpendingHeader = $x("//h2[text()='History of Spendings']");
    private final SelenideElement personIcon = $("[data-testid='PersonIcon']");
    private final SelenideElement profileLink = $("a.nav-link[href='/profile']");

    public EditSpendingPage editSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).$$("td").get(5).click();
        return new EditSpendingPage();
    }

    public ProfilePage goToProfile() {
        personIcon.click();
        profileLink.click();
        return new ProfilePage();
    }

    public void checkThatTableContainsSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).should(visible);
    }

    public MainPage checkStatisticsHeaderContainsText(String value) {
        statisticsHeader.shouldHave(text(value)).shouldBe(visible);
        return this;
    }

    public MainPage checkHistoryOfSpendingHeaderContainsText(String value) {
        historyOfSpendingHeader.shouldHave(text(value)).shouldBe(visible);
        return this;
    }
}