package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.auth.LogoutForm;
import org.junit.jupiter.api.Assertions;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AccountMenuPage {

    private final SelenideElement accountMenuForm = $("#account-menu").as("['Account menu' form]");
    private final SelenideElement profileButton = accountMenuForm.$("a[href='/profile']").as("['Profile' button]");
    private final SelenideElement friendsButton = accountMenuForm.$("a[href$='/friends']").as("['Friends' button]");
    private final SelenideElement allPeopleButton = accountMenuForm.$("a[href$='/all']").as("['All people' button]");
    private final SelenideElement signOutButton = accountMenuForm.$(byText("Sign out")).as("['Sign out' button]");

    public ProfilePage goToProfilePage() {
        profileButton.click();
        return new ProfilePage();
    }

    public LogoutForm signOut() {
        signOutButton.click();
        return new LogoutForm();
    }

    public AccountMenuPage accountMenuShouldBeVisible() {

        Assertions.assertAll("Assert account menu elements are visible", () -> {
                    assertTrue(accountMenuForm.isDisplayed());
                    assertTrue(profileButton.isDisplayed());
                    assertTrue(friendsButton.isDisplayed());
                    assertTrue(allPeopleButton.isDisplayed());
                    assertTrue(signOutButton.isDisplayed());
                }
        );

        return this;

    }

}
