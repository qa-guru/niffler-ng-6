package guru.qa.niffler.test.web;

import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.UserModel;
import guru.qa.niffler.page.auth.RegisterPage;
import guru.qa.niffler.utils.UserUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;

@ExtendWith({
        BrowserExtension.class
})
class RegisterTests {

    static final String REGISTRATION_PAGE_URL = Config.getInstance().authUrl() + "register";
    final RegisterPage registerPage = new RegisterPage();

    @Test
    void canRegisterUserWithCorrectCredentialsTest() {
        UserModel user = UserUtils.generateValidUser();
        open(REGISTRATION_PAGE_URL, RegisterPage.class)
                .signUp(user);

        registerPage.shouldVisiblePageElements();
    }

    @Test
    void canNotRegisterIfUsernameIsExistTest() {
        var user = UserUtils.generateValidUser();
        open(REGISTRATION_PAGE_URL, RegisterPage.class)
                .signUp(user)
                .goToLoginPage()
                .goToRegisterPage()
                .signUp(user);

        registerPage.assertUsernameHasError("Username `%s` already exists".formatted(user.getUsername()));
    }

    @Test
    void canNotRegisterIfPasswordAndPasswordConfirmationNotEqualTest() {
        var user = UserUtils.generateValidUser();
        open(REGISTRATION_PAGE_URL, RegisterPage.class)
                .signUp(user.setPassword(new Faker().internet().password()));
        registerPage.assertPasswordHasError("Passwords should be equal");
    }

    @Test
    void canNotRegisterIfUsernameIsTooShortTest() {
        var user = UserUtils.generateValidUser();
        user.setUsername(new Faker().lorem().characters(2));
        open(REGISTRATION_PAGE_URL, RegisterPage.class)
                .signUp(user);
        registerPage.assertUsernameHasError("Allowed username length should be from 3 to 50 characters");
    }

    @Test
    void canNotRegisterIfUsernameIsTooLongTest() {
        var user = UserUtils.generateValidUser();
        user.setUsername(new Faker().lorem().characters(51));
        open(REGISTRATION_PAGE_URL, RegisterPage.class)
                .signUp(user);
        registerPage.assertUsernameHasError("Allowed username length should be from 3 to 50 characters");
    }

    @Test
    void canNotRegisterIfPasswordIsTooShortTest() {

        var user = UserUtils.generateValidUser();
        var password = new Faker().lorem().characters(2);
        user.setPassword(password).setPasswordConfirmation(password);

        open(REGISTRATION_PAGE_URL, RegisterPage.class)
                .signUp(user);

        registerPage
                .assertPasswordHasError("Allowed password length should be from 3 to 12 characters")
                .assertPasswordConfirmationHasError("Allowed password length should be from 3 to 12 characters");
    }

    @Test
    void canNotRegisterIfPasswordIsTooLongTest() {

        var user = UserUtils.generateValidUser();
        var password = new Faker().lorem().characters(13);
        user.setPassword(password).setPasswordConfirmation(password);

        open(REGISTRATION_PAGE_URL, RegisterPage.class)
                .signUp(user);

        registerPage
                .assertPasswordHasError("Allowed password length should be from 3 to 12 characters")
                .assertPasswordConfirmationHasError("Allowed password length should be from 3 to 12 characters");
    }

}
