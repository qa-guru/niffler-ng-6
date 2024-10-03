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

    @Test
    void canRegisterUserWithCorrectCredentialsTest() {
        UserModel user = UserUtils.generateValidUser();
        open(REGISTRATION_PAGE_URL, RegisterPage.class)
                .signUp(user)
                .assertSuccessfulRegistration();
    }

    @Test
    void canNotRegisterIfUsernameIsExistTest() {
        UserModel user = UserUtils.generateValidUser();
        open(REGISTRATION_PAGE_URL, RegisterPage.class)
                .signUp(user)
                .goToLoginPage()
                .goToRegisterPage()
                .signUp(user);

        new RegisterPage().assertUsernameHasError("Username `%s` already exists".formatted(user.getUsername()));
    }

    @Test
    void canNotRegisterIfPasswordAndPasswordConfirmationNotEqualTest() {
        UserModel user = UserUtils.generateValidUser();
        open(REGISTRATION_PAGE_URL, RegisterPage.class)
                .signUp(user.setPassword(new Faker().internet().password()));
        new RegisterPage().assertPasswordHasError("Passwords should be equal");
    }

    @Test
    void canNotRegisterIfUsernameIsTooShortTest() {
        UserModel user = UserUtils.generateValidUser();
        user.setUsername(new Faker().lorem().characters(2));
        open(REGISTRATION_PAGE_URL, RegisterPage.class)
                .signUp(user);
        new RegisterPage().assertUsernameHasError("Allowed username length should be from 3 to 50 characters");
    }

    @Test
    void canNotRegisterIfUsernameIsTooLongTest() {
        UserModel user = UserUtils.generateValidUser();
        user.setUsername(new Faker().lorem().characters(51));
        open(REGISTRATION_PAGE_URL, RegisterPage.class)
                .signUp(user);
        new RegisterPage().assertUsernameHasError("Allowed username length should be from 3 to 50 characters");
    }

    @Test
    void canNotRegisterIfPasswordIsTooShort() {

        UserModel user = UserUtils.generateValidUser();
        var password = new Faker().lorem().characters(2);
        user.setPassword(password).setPasswordConfirmation(password);

        open(REGISTRATION_PAGE_URL, RegisterPage.class)
                .signUp(user);

        new RegisterPage()
                .assertPasswordHasError("Allowed password length should be from 3 to 12 characters")
                .assertPasswordConfirmationHasError("Allowed password length should be from 3 to 12 characters");
    }

    @Test
    void canNotRegisterIfPasswordIsTooLongTest() {

        UserModel user = UserUtils.generateValidUser();
        var password = new Faker().lorem().characters(13);
        user.setPassword(password).setPasswordConfirmation(password);

        open(REGISTRATION_PAGE_URL, RegisterPage.class)
                .signUp(user);

        new RegisterPage()
                .assertPasswordHasError("Allowed password length should be from 3 to 12 characters")
                .assertPasswordConfirmationHasError("Allowed password length should be from 3 to 12 characters");
    }

}
