package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotations.Category;
import guru.qa.niffler.jupiter.extensions.BrowserExtension;
import guru.qa.niffler.jupiter.extensions.UserQueueExtension;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.qameta.allure.Allure.step;

@DisplayName("Страница профиля")
@ExtendWith({BrowserExtension.class, UserQueueExtension.class})
public class ProfileWebTest extends BaseWebTest {

    private final String vladislavUsername = "vladislav";
    private final String rootPassword = "root";

    @Category(
            username = "vladislav",
            archived = false
    )
    @Test
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .setUsername(vladislavUsername)
                .setPassword(rootPassword)
                .clickLogInButton()
                .clickAvatarButton()
                .clickProfileButton()
                .archivedCategoryByName(category.name())
                .clickArchiveButton()
                .shouldHaveMessageAfterArchivedCategory(category.name())
                .showArchivedCategories();

        step("Категория, которая была заархивирована, отображается в списке архивных категорий", () -> {
            page.profilePage.shouldHaveArchiveCategoryByName(category.name());
        });
    }

    @Category(
            username = "vladislav",
            archived = true
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .setUsername(vladislavUsername)
                .setPassword(rootPassword)
                .clickLogInButton()
                .clickAvatarButton()
                .clickProfileButton()
                .showArchivedCategories()
                .unarchivedCategoryByName(category.name())
                .clickUnarchivedButton()
                .shouldHaveMessageAfterUnarchivedCategory(category.name())
                .showUnarchivedCatogories();

        step("Категория, которая была разархивирована, отображается в списке активных категорий", () -> {
            page.profilePage.shouldHaveActiveCategoryByName(category.name());
        });
    }
}
