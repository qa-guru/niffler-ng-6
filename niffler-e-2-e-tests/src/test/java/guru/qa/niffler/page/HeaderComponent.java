package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class HeaderComponent {

    // Global Header fields
    private final SelenideElement userAvatar = $("button[aria-label='Menu']");
    private final SelenideElement userMenuProfile = $x("//ul[@role='menu']/li/a[@href='/profile']");
    private final SelenideElement userMenuFriends = $x("//ul[@role='menu']/li/a[@href='/people/friends']");
    private final SelenideElement userMenuPeople = $x("//ul[@role='menu']/li/a[@href='/people/all']");
    private final SelenideElement userMenuSignOut = $x("//li[text()='Sign out']");
    private final SelenideElement newSpendingButton = $("a[href='/spending']");
    private final SelenideElement headerLogoLink = $x("//a[@href='/main']/img[@alt='Niffler logo']/..");

    public void checkThatGlobalHeaderDisplayed() {
        userAvatar.shouldBe(visible);
        newSpendingButton.shouldBe(visible);
        headerLogoLink.shouldBe(visible);
    }

    public HeaderComponent clickUserAvatar() {
        userAvatar.click();
        return this;
    }

    public UserProfilePage clickUserMenuProfile() {
        userMenuProfile.click();
        return new UserProfilePage();
    }
}