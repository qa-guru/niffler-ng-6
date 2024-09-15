package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {

    private final SelenideElement personIcon = $("[data-testid='PersonIcon']");
    private final SelenideElement friendButton = $("li [href='/people/friends']");
    private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
    private final SelenideElement mainPageRootContainer = $("[class^='MuiBox-root']");

    public EditSpendingPage editSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).$$("td").get(5).click();
        return new EditSpendingPage();
    }

    public void checkThatTableContainsSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).should(visible);
    }

    public void checkThatPageContainsRootContainer() {
        mainPageRootContainer.shouldBe(visible);
    }

    public FriendPage clickOnFriendButtonUnderPersonIcon() {
        personIcon.click();
        friendButton.click();
        return new FriendPage();
    }
}
