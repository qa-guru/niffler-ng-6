package guru.qa.niffler.test.web;

import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.auth.ConfirmRegistrationPage;
import guru.qa.niffler.page.auth.RegisterPage;
import guru.qa.niffler.utils.UserUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;

@ExtendWith({
        BrowserExtension.class
})
class RegisterWebTests {

    static final String REGISTRATION_PAGE_URL = Config.getInstance().authUrl() + "register";
    static final Faker FAKE = new Faker();
    final RegisterPage registerPage = new RegisterPage();
    final ConfirmRegistrationPage confirmRegistrationPage = new ConfirmRegistrationPage();

    @Test
    void canRegisterUserWithCorrectCredentialsTest() {
        UserJson user = UserUtils.generateUser();
        open(REGISTRATION_PAGE_URL, RegisterPage.class)
                .signUp(user);

        confirmRegistrationPage.shouldVisiblePageElements();
    }

    @Test
    void canNotRegisterIfUsernameIsExistTest() {
        var user = UserUtils.generateUser();
        open(REGISTRATION_PAGE_URL, RegisterPage.class)
                .signUp(user)
                .goToLoginPage()
                .goToRegisterPage()
                .signUp(user);

        registerPage.assertUsernameHasError("Username `%s` already exists".formatted(user.getUsername()));
    }

    @Test
    void canNotRegisterIfPasswordAndPasswordConfirmationNotEqualTest() {
        var user = UserUtils.generateUser();
        open(REGISTRATION_PAGE_URL, RegisterPage.class)
                .signUp(user.setPassword(FAKE.internet().password()));
        registerPage.assertPasswordHasError("Passwords should be equal");
    }

    @Test
    void canNotRegisterIfUsernameIsTooShortTest() {
        var user = UserUtils.generateUser();
        user.setUsername(FAKE.lorem().characters(2));
        open(REGISTRATION_PAGE_URL, RegisterPage.class)
                .signUp(user);
        registerPage.assertUsernameHasError("Allowed username length should be from 3 to 50 characters");
    }

    @Test
    void canNotRegisterIfUsernameIsTooLongTest() {
        var user = UserUtils.generateUser();
        user.setUsername(FAKE.lorem().characters(51));
        open(REGISTRATION_PAGE_URL, RegisterPage.class)
                .signUp(user);
        registerPage.assertUsernameHasError("Allowed username length should be from 3 to 50 characters");
    }

    @Test
    void canNotRegisterIfPasswordIsTooShortTest() {

        var user = UserUtils.generateUser();
        var password = FAKE.lorem().characters(2);
        user.setPassword(password).setPasswordConfirmation(password);

        open(REGISTRATION_PAGE_URL, RegisterPage.class)
                .signUp(user);

        registerPage
                .assertPasswordHasError("Allowed password length should be from 3 to 12 characters")
                .assertPasswordConfirmationHasError("Allowed password length should be from 3 to 12 characters");
    }

    @Test
    void canNotRegisterIfPasswordIsTooLongTest() {

        var user = UserUtils.generateUser();
        var password = FAKE.lorem().characters(13);
        user.setPassword(password).setPasswordConfirmation(password);

        open(REGISTRATION_PAGE_URL, RegisterPage.class)
                .signUp(user);

        registerPage
                .assertPasswordHasError("Allowed password length should be from 3 to 12 characters")
                .assertPasswordConfirmationHasError("Allowed password length should be from 3 to 12 characters");
    }

}