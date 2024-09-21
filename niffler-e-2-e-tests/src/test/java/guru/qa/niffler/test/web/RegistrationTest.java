package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.BaseTest;
import org.junit.jupiter.api.Test;

@WebTest
public class RegistrationTest extends BaseTest {
    Faker faker = new Faker();
    String login = faker.name().username();
    String password = faker.internet().password(3, 12);

    @Test
    void shouldRegisterNewUser() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickRegistrationBtn()
                .registration(login, password, password)
                .registrationIsSuccessCheck();
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        String userName = "lissamissa";
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickRegistrationBtn()
                .registration(userName, password, password)
                .registrationErrorCheck("Username `" + userName + "` already exists");
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        String anotherPassword = faker.internet().password(3, 12);
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickRegistrationBtn()
                .registration(login, password, anotherPassword)
                .registrationErrorCheck("Passwords should be equal");
    }
}
