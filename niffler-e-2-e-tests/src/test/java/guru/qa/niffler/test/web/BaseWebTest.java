package guru.qa.niffler.test.web;

import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.ProfilePage;
import guru.qa.niffler.page.RegisterPage;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
abstract class BaseWebTest {

    protected static final Config CFG = Config.getInstance();

    protected final Faker faker = new Faker();

    protected final Page page = new Page();

    protected static class Page {
        protected final RegisterPage registerPage = new RegisterPage();
        protected final LoginPage loginPage = new LoginPage();
        protected final MainPage mainPage = new MainPage();
        protected final ProfilePage profilePage = new ProfilePage();
    }
}
