package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.Pages;
import io.qameta.allure.Step;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;

@ExtendWith(BrowserExtension.class)
public class ProfileTest extends Pages {
    String userData = "kisa";

    @BeforeEach
    @Step("Открыть страницу авторизации")
    public void openLoginPage() {
        final Config CFG = Config.getInstance();
        open(CFG.frontUrl(), LoginPage.class);
    }

    @Test
    @DisplayName("После удачной авторизации показывается главная страница")
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        openLoginPage();
        loginPage.login(userData, userData);
        mainPage.mainPageAfterLoginCheck();
    }

    @Test
    @DisplayName("После неудачной авторизации показывается ошибка")
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        openLoginPage();
        loginPage.login(userData, "kiss");
        loginPage.loginErrorCheck("Bad credentials");
    }

    @DisplayName("Архивация категории")
    @Category(username = "risa",
            archived = false)
    @Test
    void archivingCategoryTest(CategoryJson category) {
        openLoginPage();
        loginPage.login("risa", "risa");
        mainPage.openProfile();
        profilePage.clickArchiveButton();
        profilePage.confirmArchiving();
        profilePage.successArchiveTooltipCheck("Category " + category.name() + " is archived");
    }

    @DisplayName("Разархивация категории")
    @Category(username = "misa",
            archived = true)
    @Test
    void unArchivingCategoryTest(CategoryJson category) {
        openLoginPage();
        loginPage.login("misa", "misa");
        mainPage.openProfile();
        profilePage.clickArchiveSwitcher();
        profilePage.categoryInListCheck(category.name());
        profilePage.clickUnArchiveButton();
        profilePage.confirmArchiving();
        profilePage.categoryNotInListCheck(category.name());
    }

    @Category(username = "nisa",
            archived = false)
    @Test
    @DisplayName("Активная категория отображается в списке активных категорий")
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
        openLoginPage();
        loginPage.login("nisa", "nisa");
        mainPage.openProfile();
        profilePage.categoryInListCheck(category.name());
    }

    @Category(username = "lisa",
            archived = true)
    @Test
    @DisplayName("Архивная категория отображается в списке архивных категорий")
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {
        openLoginPage();
        loginPage.login("lisa", "lisa");
        mainPage.openProfile();
        profilePage.categoryNotInListCheck(category.name());
        profilePage.clickArchiveSwitcher();
        profilePage.categoryInListCheck(category.name());
    }
}
