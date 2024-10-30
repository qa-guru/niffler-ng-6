package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.auth.LoginPage;
import guru.qa.niffler.page.auth.LogoutForm;
import guru.qa.niffler.page.people.AllPeoplePage;
import guru.qa.niffler.page.people.FriendsPage;
import io.qameta.allure.Step;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@Slf4j
@NoArgsConstructor
public class AccountMenuPage extends BasePage<AccountMenuPage> {

    private final SelenideElement accountMenuForm = $("#account-menu").as("['Account menu' form]");
    private final SelenideElement profileButton = accountMenuForm.$("a[href='/profile']").as("['Profile' button]");
    private final SelenideElement friendsButton = accountMenuForm.$("a[href$='/friends']").as("['Friends' button]");
    private final SelenideElement allPeopleButton = accountMenuForm.$("a[href$='/all']").as("['All people' button]");
    private final SelenideElement signOutButton = accountMenuForm.$(byText("Sign out")).as("['Sign out' button]");

    @Step("Go to 'Profile' page")
    public ProfilePage goToProfilePage() {
        log.info("Go to 'Profile' page");
        profileButton.click();
        return new ProfilePage(true);
    }

    @Step("Go to 'Friends' page")
    public FriendsPage goToFriendsPage() {
        log.info("Go to 'Friends' page");
        friendsButton.click();
        return new FriendsPage(true);
    }

    @Step("Go to 'All people' page")
    public AllPeoplePage goToAllPeoplePage() {
        log.info("Go to 'All people' page");
        allPeopleButton.click();
        return new AllPeoplePage(true);
    }

    @Step("Open 'Log out' form")
    public LogoutForm goToLogoutForm() {
        log.info("Open logout out form");
        signOutButton.click();
        return new LogoutForm(true);
    }

    @Step("Log out")
    public LoginPage logout() {
        goToLogoutForm();
        return new LogoutForm().logout();
    }

    @Override
    public AccountMenuPage shouldVisiblePageElement() {
        log.info("Assert account menu element are visible");
        signOutButton.shouldBe(visible);
        return this;
    }

    @Override
    @Step("Menu should be visible")
    public AccountMenuPage shouldVisiblePageElements() {

        log.info("Assert account menu elements are visible");

        accountMenuForm.shouldBe(visible);
        profileButton.shouldBe(visible);
        friendsButton.shouldBe(visible);
        allPeopleButton.shouldBe(visible);
        signOutButton.shouldBe(visible);

        return this;
    }

}