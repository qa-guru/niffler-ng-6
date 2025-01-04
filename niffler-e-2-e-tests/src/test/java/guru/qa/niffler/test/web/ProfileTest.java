package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@WebTest
public class ProfileTest extends BaseTest {

    @Category(
            username = "cat",
            archived = false)
    @Test
    @DisplayName("Archive category should present in categories list")
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
    @DisplayName("Active category should present in categories list")
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
