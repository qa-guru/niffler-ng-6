package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.SignInPage;
import org.junit.jupiter.api.Test;

public class ProfileTest {

    private static final Config CFG = Config.getInstance();

    @Category(
            username = "duck",
            archived = true
    )
    @Test
    void archivedCategoryShouldBePresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), SignInPage.class)
                .signIn("duck", "12345")
                .getPageHeader()
                .clickUserAvatar()
                .clickUserMenuProfile()
                .checkThatCategoryIsNotPresentInCategoriesList(category.name())
                .toggleShowArchived()
                .checkThatCategoryIsPresentInCategoriesList(category.name())
                .toggleShowArchived()
                .checkThatCategoryIsNotPresentInCategoriesList(category.name());
    }

    @Category(
            username = "duck",
            archived = false
    )
    @Test
    void activeCategoryShouldBePresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), SignInPage.class)
                .signIn("duck", "12345")
                .getPageHeader()
                .clickUserAvatar()
                .clickUserMenuProfile()
                .checkThatCategoryIsPresentInCategoriesList(category.name());
    }
}