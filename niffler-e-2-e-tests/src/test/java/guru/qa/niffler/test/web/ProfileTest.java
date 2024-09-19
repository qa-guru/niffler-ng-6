package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.AddCategory;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;

@DisplayName("Профиль")
public class ProfileTest extends BaseWebTest {

    @Test
    @Story("Категории")
    @DisplayName("Архивная категория должна присутствовать в списке заархивированных категорий в профиле пользователя")
    @User(
            username = "oleg",
            categories = {
                    @AddCategory(isCategoryArchive = true)
            }
    )
    void testArchivedCategoryShouldPresentInCategoriesList(CategoryJson category) {
        step("Открыть страницу авторизации", () -> {
            Selenide.open(CFG.frontUrl(), LoginPage.class);
        });
        step("Авторизоваться в системе", () -> {
            page.loginPage.login("oleg", "12345");
        });
        step("Перейти в профиль пользователя и активировать checkbox 'Show archived'", () ->
                page.mainPage.openProfile()
                        .setArvedCategoriesVisible()
        );
        step("Проверить, что заархивированная категория отображается в списке", () ->
                page.profilePage.categoriesShouldBeVisible(category.name())
        );
    }

    @Test
    @Story("Категории")
    @DisplayName("Активная категория должна присутствовать в списке категорий в профиле пользователя")
    @User(
            username = "oleg",
            categories = {
                    @AddCategory(isCategoryArchive = false)
            }
    )
    void testActiveCategoryShouldPresentInCategoriesList(CategoryJson category) {
        step("Открыть страницу авторизации", () -> {
            Selenide.open(CFG.frontUrl(), LoginPage.class);
        });
        step("Авторизоваться в системе", () -> {
            page.loginPage.login("oleg", "12345");
        });
        step("Перейти в профиль пользователя и проверить что категория отображается в активных", () ->
                page.mainPage.openProfile()
                        .categoriesShouldBeVisible(category.name())
        );
    }
}
