package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.*;

public class BaseTest {

    protected static final Config CFG = Config.getInstance();
    protected final MainPage mainPage = new MainPage();
    protected final ProfilePage profilePage = new ProfilePage();
    protected final RegisterPage registerPage = new RegisterPage();
    protected final LoginPage loginPage = new LoginPage();
    protected final FriendsPage friendsPage = new FriendsPage();
}
