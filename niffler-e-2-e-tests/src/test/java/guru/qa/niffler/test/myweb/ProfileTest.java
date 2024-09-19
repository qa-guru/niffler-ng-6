package guru.qa.niffler.test.myweb;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.myannotations.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.mypages.LoginPage;
import guru.qa.niffler.mypages.MainPage;
import guru.qa.niffler.mypages.ProfilePage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class ProfileTest {

    private static final Config CFG = Config.getInstance();

    @Category(
            username = "duck",
            title = "Mleko",
            archived = false
    )

    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson categoryJson) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("duck", "12345");
        MainPage mainPage = new MainPage();
        mainPage.checkMainPage();
        ProfilePage profilePage = mainPage.selectProfile();
        profilePage.checkCat(categoryJson.name());
    }

    @Category(
            username = "duck",
            title = "",
            archived = true
    )

    @Test
    void acrchivedCategoryShouldPresentInCategoriesList(CategoryJson categoryJson) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("duck", "12345");
        MainPage mainPage = new MainPage();
        mainPage.checkMainPage();
        ProfilePage profilePage = mainPage.selectProfile();
        profilePage.showArchived()
                .checkCat(categoryJson.name());
    }
}
