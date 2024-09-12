package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.TopMenu;
import org.junit.jupiter.api.Test;

public class ProfileWebTest {
    private static final Config CFG = Config.getInstance();

    @Category(
            username = "duck",
            archived = false
    )
    @Test
    void archivedCategoryShouldNotPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("duck", "12345");
        new TopMenu().goToProfile()
                .clickArchiveCategoryByName(category.name())
                .clickArchiveButtonSubmit()
                .shouldBeVisibleArchiveSuccessMessage(category.name())
                .shouldNotBeVisibleArchiveCategory(category.name());
    }

    @Category(
            username = "duck",
            archived = true
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("duck", "12345");
        new TopMenu().goToProfile()
                .clickShowArchiveCategoryButton()
                .clickUnarchiveCategoryByName(category.name())
                .clickUnarchiveButtonSubmit()
                .shouldBeVisibleUnarchiveSuccessMessage(category.name())
                .clickShowArchiveCategoryButton()
                .shouldBeVisibleActiveCategory(category.name());
    }
}
