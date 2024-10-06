package guru.qa.niffler.mypages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.EditSpendingPage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class MainPage {
    private final SelenideElement nifflerHead = $x("//div[@id='root']//*[text()='Niffler']");
    private final SelenideElement statsPieBlock = $("#stat");
    private final SelenideElement historyOfSpendingsBlock = $("#spendings");

    public final SelenideElement clientMenuBtn = $("button[class*='root']");
    public final SelenideElement profileBtn = $("a[href='/profile']");
    public final SelenideElement friendsBtn = $("a[href='/people/friends']");
    public final SelenideElement allPeopleBtn = $("a[href='/people/all']");
    public final SelenideElement signOutBtn = $x("//li[text()='Sign out']");

    private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");

    public EditSpendingPage editSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).$$("td").get(5).click();
        return new EditSpendingPage();
    }

    public void checkThatTableContainsSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).should(visible);
    }

    public void checkMainPage() {
        nifflerHead.shouldBe(visible);
        statsPieBlock.shouldBe(visible);
        historyOfSpendingsBlock.shouldBe(visible);
    }

    public ProfilePage selectProfile() {
        clientMenuBtn.shouldBe(visible)
                .click();
        profileBtn.shouldBe(visible)
                .click();
        return new ProfilePage();
    }

    public FriendsPage selectFriends() {
        clientMenuBtn.shouldBe(visible)
                .click();
        friendsBtn.shouldBe(visible)
                .click();
        return new FriendsPage();
    }
}
