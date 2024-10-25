package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.Test;

@WebTest
public class ProfileWebTest {
    private static final Config CFG = Config.getInstance();

    @User(
            username = "duck",
            categories = @Category(
                    archived = true
            )
    )
    @Test
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("duck", "123456");

        MainPage mainPage = new MainPage();
        mainPage.clickProfileButton();

        ProfilePage profilePage = new ProfilePage();
        profilePage.clickArchiveButton()
                .clickArchiveButtonSubmit()
                .shouldBeVisibleSuccessMessage()
                .clickShowArchivedButton()
                .shouldForCategoryName(category.name());
    }

    @User(
            username = "duck",
            categories = @Category(
                    archived = false
            )
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("duck", "123456");

        MainPage mainPage = new MainPage();
        mainPage.clickProfileButton();

        ProfilePage profilePage = new ProfilePage();
        profilePage.shouldActiveCategoryList(category.name());
    }
}

