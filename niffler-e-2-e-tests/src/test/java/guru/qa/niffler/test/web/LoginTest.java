package guru.qa.niffler.test.web;

import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.converter.BrowserConverter;
import guru.qa.niffler.jupiter.converter.Browsers;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.EnumSource;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

@WebTest
public class LoginTest {

    @User
    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin(UserJson user) {
        SelenideDriver driver = new SelenideDriver(Browsers.CHROME.driver());
        driver.open(LoginPage.URL);
        new LoginPage(driver)
                .fillLoginPage(user.username(), user.testData().password())
                .submit(new MainPage(driver))
                .checkThatPageLoaded();
    }

    @ParameterizedTest
    @EnumSource(Browsers.class)
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials(@ConvertWith(BrowserConverter.class) SelenideDriver driver) {
        driver.open(LoginPage.URL);
        new LoginPage(driver)
                .fillLoginPage(randomUsername(), "BAD")
                .submit(new LoginPage(driver))
                .checkError("Bad credentials");
    }
}
