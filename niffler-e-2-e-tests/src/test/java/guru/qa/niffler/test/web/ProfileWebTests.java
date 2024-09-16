package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.BaseWebTest;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

public class ProfileWebTests extends BaseWebTest {
    private static final Config CFG = Config.getInstance();
    private static final String USERNAME = "duck";
    private static final String PASSWORD = "12345";
    private static final String ARCHIVED_SUCCESS_MESSAGE = "Category %s is archived";
    private static final String UNARCHIVED_SUCCESS_MESSAGE = "Category %s is unarchived";

    @Category(
            username = "duck",
            archived = false
    )
    @Test
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(USERNAME, PASSWORD)
                .clickToMenuButton()
                .clickToProfileButton()
                .clickArchiveCategoryByName(category.name())
                .clickArchiveSubmitButton()
                .successMessageShouldBeVisible(String.format(ARCHIVED_SUCCESS_MESSAGE, category.name()))
                .checkCategoryNotVisible(category.name())
                .clickShowArchiveCategorySwitcher()
                .checkCategoryVisible(category.name());
    }

    @Category(
            username = "duck",
            archived = true)
    @Test
    void categoryIsUnarchivedSuccessfully(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(USERNAME, PASSWORD)
                .clickToMenuButton()
                .clickToProfileButton()
                .clickShowArchiveCategorySwitcher()
                .clickUnarchiveCategory(category.name())
                .clickUnArchiveSubmitButton()
                .successMessageShouldBeVisible(String.format(UNARCHIVED_SUCCESS_MESSAGE, category.name()))
                .checkCategoryVisible(category.name());
    }
}
