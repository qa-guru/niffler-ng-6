package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.SignInPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(BrowserExtension.class)
public class SignInWebTest {

    private static final Config CFG = Config.getInstance();

    @Test
    void shouldDisplayMainPageOnSuccessfulSignIn() {

        Selenide.open(CFG.frontUrl(), SignInPage.class)
                .signIn("duck", "12345");

        new MainPage()
                .checkStatisticsModuleDisplayed()
                .checkSpendingModuleDisplayed()
                .checkThatGlobalHeaderDisplayed();
    }

    @Test
    void userShouldStayOnLoginPageAfterSigningInWithBadCredentials() {
        Selenide.open(CFG.frontUrl(), SignInPage.class)
                .signIn("duck", "547658h67d38456h384765h873465f873465h89f");

        assertTrue(new SignInPage().isBadCredentialsErrorMessageDisplayed());
    }

}