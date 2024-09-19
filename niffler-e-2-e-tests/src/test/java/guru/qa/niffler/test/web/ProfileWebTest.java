package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.Category;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class ProfileWebTest {
    private static final Config CFG = Config.getInstance();
    private final MainPage mainPage = new MainPage();
    private final ProfilePage profilePage = new ProfilePage();

    @Category(
            username = "duck",
            name = "TestCategory",
            archived = false)
    @Test
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("duck", "123456");
        mainPage.clickProfileButton();
        profilePage.clickArchiveButton()
                .clickArchiveButtonSubmit()
                .shouldBeVisibleSuccessMessage()
                .clickShowArchivedButton()
                .shouldForCategoryName(category.name());
    }

    @Category(
            username = "duck",
            name = "TestCategory",
            archived = false)
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("duck", "123456");

        mainPage.clickProfileButton();
        profilePage.shouldActiveCategoryList(category.name());
    }
}

