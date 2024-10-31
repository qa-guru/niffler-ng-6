package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.SignInPage;
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

    @User(
            username = "duck",
            categories = @Category(
                    archived = false
            )
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