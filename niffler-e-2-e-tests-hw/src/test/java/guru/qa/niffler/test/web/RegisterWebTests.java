package guru.qa.niffler.test.web;

import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.page.auth.ConfirmRegistrationPage;
import guru.qa.niffler.page.page.auth.RegisterPage;
import guru.qa.niffler.utils.UserUtils;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

@WebTest
class RegisterWebTests {

    static final Faker FAKE = new Faker();
    final RegisterPage registerPage = new RegisterPage();
    final ConfirmRegistrationPage confirmRegistrationPage = new ConfirmRegistrationPage();

    @Test
    void canRegisterUserWithCorrectCredentialsTest() {
        UserJson user = UserUtils.generateUser();
        open(RegisterPage.URL, RegisterPage.class)
                .signUpSuccess(user);

        confirmRegistrationPage.shouldVisiblePageElements();
    }

    @Test
    void canNotRegisterIfUsernameIsExistTest() {
        var user = UserUtils.generateUser();
        open(RegisterPage.URL, RegisterPage.class)
                .signUpSuccess(user)
                .goToLoginPage()
                .goToRegisterPage()
                .signUpSuccess(user);

        registerPage.assertUsernameHasError("Username `%s` already exists".formatted(user.getUsername()));
    }

    @Test
    void canNotRegisterIfPasswordAndPasswordConfirmationNotEqualTest() {
        var user = UserUtils.generateUser();
        open(RegisterPage.URL, RegisterPage.class)
                .signInFailed(user.setPassword(FAKE.internet().password()));
        registerPage.assertPasswordHasError("Passwords should be equal");
    }

    @Test
    void canNotRegisterIfUsernameIsTooShortTest() {
        var user = UserUtils.generateUser();
        user.setUsername(FAKE.lorem().characters(2));
        open(RegisterPage.URL, RegisterPage.class)
                .signUpSuccess(user);
        registerPage.assertUsernameHasError("Allowed username length should be from 3 to 50 characters");
    }

    @Test
    void canNotRegisterIfUsernameIsTooLongTest() {
        var user = UserUtils.generateUser();
        user.setUsername(FAKE.lorem().characters(51));
        open(RegisterPage.URL, RegisterPage.class)
                .signUpSuccess(user);
        registerPage.assertUsernameHasError("Allowed username length should be from 3 to 50 characters");
    }

    @Test
    void canNotRegisterIfPasswordIsTooShortTest() {

        var user = UserUtils.generateUser();
        var password = FAKE.lorem().characters(2);
        user.setPassword(password).setPasswordConfirmation(password);

        open(RegisterPage.URL, RegisterPage.class)
                .signUpSuccess(user);

        registerPage
                .assertPasswordHasError("Allowed password length should be from 3 to 12 characters")
                .assertPasswordConfirmationHasError("Allowed password length should be from 3 to 12 characters");
    }

    @Test
    void canNotRegisterIfPasswordIsTooLongTest() {

        var user = UserUtils.generateUser();
        var password = FAKE.lorem().characters(13);
        user.setPassword(password).setPasswordConfirmation(password);

        open(RegisterPage.URL, RegisterPage.class)
                .signUpSuccess(user);

        registerPage
                .assertPasswordHasError("Allowed password length should be from 3 to 12 characters")
                .assertPasswordConfirmationHasError("Allowed password length should be from 3 to 12 characters");
    }

}