package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class ProfileTest {

    private static final Config CFG = Config.getInstance();
    MainPage mainPage = new MainPage();
    ProfilePage profilePage = new ProfilePage();

    @Category(
            username = "cat",
            archived = false)
    @Test
    void archiveCategoryShouldPresentInCategoriesList(CategoryJson category) {

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin("cat", "12345");

        mainPage.openProfilePage();
        profilePage.clickArchiveButton(category.name());
        profilePage.confirmArchivation();
        profilePage.clickIconShowArchived();
        profilePage.checkThatCategoryIsArchived(category.name());
    }

    @Category(
            username = "cat",
            archived = true)
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin("cat", "12345");

        mainPage.openProfilePage();
        profilePage.clickIconShowArchived();
        profilePage.clickActiveButton(category.name());
        profilePage.confirmActivation();
        profilePage.checkThatCategoryIsActive(category.name());
    }
}
