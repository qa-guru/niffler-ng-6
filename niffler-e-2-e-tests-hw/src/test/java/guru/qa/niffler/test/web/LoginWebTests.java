package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.page.MainPage;
import guru.qa.niffler.page.page.auth.LoginPage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
@WebTest
class LoginWebTests {

    static final Faker FAKE = new Faker();
    final MainPage mainPage = new MainPage();
    final LoginPage loginPage = new LoginPage();

    @Test
    void shouldLoginWithCorrectCredentialsTest(@CreateNewUser UserJson user) {
        Selenide.open(LoginPage.URL, LoginPage.class)
                .login(user.getUsername(), user.getPassword());
        mainPage.shouldVisiblePageElements();
    }

    @Test
    void shouldDisplayErrorMessageIfPasswordIsIncorrectTest(@CreateNewUser UserJson user) {
        Selenide.open(LoginPage.URL, LoginPage.class)
                .login(user.getUsername(), FAKE.internet().password());
        loginPage.shouldVisibleBadCredentialsError();
    }

    @Test
    void shouldDisplayErrorMessageIfUsernameNotFoundTest() {
        Selenide.open(LoginPage.URL, LoginPage.class)
                .login(FAKE.name().username() + FAKE.number().randomNumber(), FAKE.internet().password());
        loginPage.shouldVisibleBadCredentialsError();
    }

    @Test
    void canSignOutTest(@CreateNewUser UserJson user) {
        Selenide.open(LoginPage.URL, LoginPage.class)
                .login(user.getUsername(), user.getPassword())
                .getHeader()
                .signOut();
        loginPage.shouldVisiblePageElements();
    }

}