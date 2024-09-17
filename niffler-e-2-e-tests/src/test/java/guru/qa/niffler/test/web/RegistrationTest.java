package guru.qa.niffler.test.web;

import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.Pages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;

@ExtendWith(BrowserExtension.class)
public class RegistrationTest extends Pages {
    Faker faker = new Faker();
    String login = faker.name().username();
    String password = faker.internet().password(3, 12);

    private static final Config CFG = Config.getInstance();

    @Test
    void shouldRegisterNewUser() {
        startRegistration();
        registrationPage.registration(login, password, password);
        registrationPage.registrationIsSuccessCheck();
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        String userName = "lissamissa";
        startRegistration();
        registrationPage.registration(userName, password, password);
        registrationPage.registrationErrorCheck("Username `" + userName + "` already exists");
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        String anotherPassword = faker.internet().password(3, 12);
        startRegistration();
        registrationPage.registration(login, password, anotherPassword);
        registrationPage.registrationErrorCheck("Passwords should be equal");
    }

    @BeforeEach
    public void startRegistration() {
        step("Открыть страницу регистрации", () -> {
            open(CFG.frontUrl(), LoginPage.class)
                    .clickRegistrationBtn();
        });
    }
}
