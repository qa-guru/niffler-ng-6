package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class MainPage {

    private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
    private final SelenideElement iconUser = $("[data-testid='PersonIcon']");
    private final SelenideElement profileSection = $x(".//a[text()='Profile']");
    private final SelenideElement friendsSection = $x(".//a[text()='Friends']");
    private final SelenideElement statisticsComponent = $x(".//h2[text()='Statistics']");
    private final SelenideElement historyOfSpendingsComponent = $x(".//h2[text()='History of Spendings']");

    public EditSpendingPage editSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).$$("td").get(5).click();
        return new EditSpendingPage();
    }

    public void checkThatTableContainsSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).should(visible);
    }

    @Step("Open profile page")
    public void openProfilePage() {
        iconUser.click();
        profileSection.click();
    }

    @Step("Open friends page")
    public void openFriendsPage() {
        iconUser.click();
        friendsSection.click();
    }

    public void checkingDisplayOfMainComponents() {
        statisticsComponent.shouldBe(visible);
        historyOfSpendingsComponent.shouldBe(visible);
    }
}
