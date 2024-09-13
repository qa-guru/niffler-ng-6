package guru.qa.niffler.test.web;

import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

public class LoginWebTests {
    private static final Config CFG = Config.getInstance();
    private static final Faker faker = new Faker();
    private static final LoginPage loginPage = new LoginPage();

    private static final String username = "Dramasha";
    private final String invalidPassword = faker.internet().password();

    @Test
    void checkCreateUser() {
        open(CFG.frontUrl(), LoginPage.class)
                .login(username, invalidPassword);
        loginPage.checkErrorBadCredentials();
    }

}
