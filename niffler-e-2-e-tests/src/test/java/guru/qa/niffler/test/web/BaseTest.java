package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.*;

public class BaseTest {

    protected static final Config CFG = Config.getInstance();
    MainPage mainPage = new MainPage();
    ProfilePage profilePage = new ProfilePage();
    RegisterPage registerPage = new RegisterPage();
    LoginPage loginPage = new LoginPage();
    FriendsPage friendsPage = new FriendsPage();
}
