package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.auth.LogoutForm;
import lombok.extern.slf4j.Slf4j;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@Slf4j
public class AccountMenuPage extends BasePage<AccountMenuPage> {

    private final SelenideElement accountMenuForm = $("#account-menu").as("['Account menu' form]");
    private final SelenideElement profileButton = accountMenuForm.$("a[href='/profile']").as("['Profile' button]");
    private final SelenideElement friendsButton = accountMenuForm.$("a[href$='/friends']").as("['Friends' button]");
    private final SelenideElement allPeopleButton = accountMenuForm.$("a[href$='/all']").as("['All people' button]");
    private final SelenideElement signOutButton = accountMenuForm.$(byText("Sign out")).as("['Sign out' button]");

    public ProfilePage goToProfilePage() {
        log.info("Go to profile page");
        profileButton.click();
        return new ProfilePage();
    }

    public LogoutForm signOut() {
        log.info("Sign out");
        signOutButton.click();
        return new LogoutForm();
    }

    @Override
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
