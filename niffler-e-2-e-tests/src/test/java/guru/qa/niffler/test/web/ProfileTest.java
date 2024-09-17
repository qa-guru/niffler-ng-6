package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.Header;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class ProfileTest {

    private static final Config CFG = Config.getInstance();
    private ProfilePage profilePage = new ProfilePage();

    @Category(
            username = "duck",
            archived = true
    )
    @Test
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(category.username(), "12345");
        new Header()
                .clickProfileMenuButton("Profile");
        boolean isActiveCategory = profilePage
                .switchArchiveSwitcher(true)
                .isCategoryActive(category.name());
        Assertions.assertFalse(isActiveCategory);
    }

    @Category(
            username = "duck",
            archived = false
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(category.username(), "12345");
        new Header()
                .clickProfileMenuButton("Profile");
        boolean isActiveCategory = profilePage
                .switchArchiveSwitcher(true)
                .isCategoryActive(category.name());
        Assertions.assertTrue(isActiveCategory);
    }
}