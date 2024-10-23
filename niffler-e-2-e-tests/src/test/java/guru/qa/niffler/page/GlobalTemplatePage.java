package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class GlobalTemplatePage {

    // Global Header fields
    private final SelenideElement headerUserAccountButton = $("button[aria-label='Menu']");
    private final SelenideElement headerUserAccountMenuProfileButton = $x("//ul[@role='menu']/li/a[@href='/profile']");
    private final SelenideElement headerUserAccountMenuFriendsButton = $x("//ul[@role='menu']/li/a[@href='/people/friends']");
    private final SelenideElement headerUserAccountMenuAllPeopleButton = $x("//ul[@role='menu']/li/a[@href='/people/all']");
    private final SelenideElement headerUserAccountMenuSignOutButton = $x("//li[text()='Sign out']");
    private final SelenideElement headerNewSpendingCtaButton = $("a[href='/spending']");
    private final SelenideElement headerLogoLink = $x("//a[@href='/main']/img[@alt='Niffler logo']/..");

    public void checkThatGlobalHeaderDisplayed() {
        headerUserAccountButton.shouldBe(Condition.visible);
        headerNewSpendingCtaButton.shouldBe(Condition.visible);
        headerLogoLink.shouldBe(Condition.visible);
    }

    public GlobalTemplatePage clickHeaderUserAccountButton() {
        headerUserAccountButton.click();
        return this;
    }

    public UserProfilePage clickHeaderUserAccountMenuProfileButton() {
        headerUserAccountMenuProfileButton.click();
        return new UserProfilePage();
    }
}