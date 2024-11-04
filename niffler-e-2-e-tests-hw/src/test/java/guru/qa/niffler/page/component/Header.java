package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.page.MainPage;
import guru.qa.niffler.page.page.ProfilePage;
import guru.qa.niffler.page.page.auth.LoginPage;
import guru.qa.niffler.page.page.auth.LogoutForm;
import guru.qa.niffler.page.page.people.AllPeoplePage;
import guru.qa.niffler.page.page.people.FriendsPage;
import guru.qa.niffler.page.page.spending.AddNewSpendingPage;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.clickable;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@Slf4j
@ParametersAreNonnullByDefault
public class Header extends BaseComponent<Header> {

    private final SelenideElement logoImg = self.$("a[href='main'] img").as("['Logo' image]");
    private final SelenideElement newSpendingButton = self.$("a[href='/spending']").as("['New spending' button]");
    private final SelenideElement menuIcon = self.$("button[aria-label='Menu']").as("['Menu' button]");
    private final SelenideElement accountMenuForm = $("#account-menu").as("['Account menu' form]");
    private final SelenideElement profileButton = accountMenuForm.$("a[href='/profile']").as("['Profile' button]");
    private final SelenideElement friendsButton = accountMenuForm.$("a[href$='/friends']").as("['Friends' button]");
    private final SelenideElement allPeopleButton = accountMenuForm.$("a[href$='/all']").as("['All people' button]");
    private final SelenideElement signOutButton = accountMenuForm.$(byText("Sign out")).as("['Sign out' button]");

    public Header() {
        super($("header").as("[Header]"));
    }

    public Header(SelenideElement self) {
        super(self);
    }

    @Step("Go to \"Main\" page")
    public MainPage goToMainPage() {
        log.info("Go to \"Main\" page");
        logoImg.click();
        return new MainPage();
    }

    @Step("Go to \"Add new spending\" page")
    public AddNewSpendingPage goToAddNewSpendingPage() {
        log.info("Go to \"Add new spending\" page");
        newSpendingButton.click();
        return new AddNewSpendingPage();
    }

    @Step("Go to \"Profile\" page")
    public ProfilePage goToProfilePage() {
        log.info("Go to \"Profile\" page");
        menuIcon.click();
        profileButton.click();
        return new ProfilePage();
    }

    @Step("Go to \"Friends\" page")
    public FriendsPage goToFriendsPage() {
        log.info("Go to \"Friends\" page");
        menuIcon.click();
        friendsButton.click();
        return new FriendsPage();
    }

    @Step("Go to \"All people\" page")
    public AllPeoplePage goToAllPeoplePage() {
        log.info("Go to \"All people\" page");
        menuIcon.click();
        allPeopleButton.click();
        return new AllPeoplePage();
    }

    public LoginPage signOut() {
        menuIcon.click();
        log.info("Open \"Sign out\" form");
        signOutButton.shouldBe(clickable).click();
        return new LogoutForm().logout();
    }

    public LogoutForm openSignOutForm() {
        menuIcon.click();
        log.info("Open \"Sign out\" form");
        signOutButton.click();
        return new LogoutForm();
    }

}
