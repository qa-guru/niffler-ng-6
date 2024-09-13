package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.BrowserExtension;
import guru.qa.niffler.jupiter.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class ProfileWebTest {
    private  static  final Config CFG = Config.getInstance();

    @Category(
            username = "esa",
            archived = true
    )
    @Test
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) throws InterruptedException {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("esa","12345")
                .openProfilePage()
                .turnOnShowArchivedCategory();
        Assertions.assertTrue(new ProfilePage().checkArchivedCategoryIsDisplay(category.name()));
    }



    @Category(
            username = "esa",
            archived = false
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("esa","12345")
                .openProfilePage()
                .turnOnShowArchivedCategory();
        Assertions.assertTrue(new ProfilePage().checkArchivedCategoryIsDisplay(category.name()));
    }
}
