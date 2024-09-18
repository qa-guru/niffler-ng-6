package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class TestProfile {

    private static final Config CFG = Config.getInstance();
    final String messageUpdateProfile = "Profile successfully updated";


    @Test
    void pictureShouldBeUploaded() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("oleg", "123456")
                .clickAvatar()
                .clickProfile()
                .editName("Petya")
                .saveName()
                .shouldSuccessProfileUpdated(messageUpdateProfile);
    }

    @Category(
            username = "superduck1",
            isArchive = false
    )
    @Test
    void archivedCategoryShouldNotPresentInCategoriesList(CategoryJson category) throws InterruptedException {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("superduck1", "12345")
                .clickAvatar()
                .clickProfile()
                .clickArchiveButtonForCategoryName(category.name())
                .clickArchiveButtonSubmit()
                .shouldBeVisibleArchiveSuccessMessage(category.name())
                .shouldNotVisibleArchiveCategory(category.name());
    }

    //
    @Category(
            username = "superduck1",
            isArchive = true
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) throws InterruptedException {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("superduck1", "12345")
                .clickAvatar()
                .clickProfile()
                .clickShowArchiveCategoryButton()
                .clickUnarchiveButtonForCategoryName(category.name())
                .clickUnarchiveButtonSubmit()
                .shouldBeVisibleUnarchiveSuccessMessage(category.name())
                .clickShowArchiveCategoryButton()
                .shouldVisibleActiveCategory(category.name());
    }
}
