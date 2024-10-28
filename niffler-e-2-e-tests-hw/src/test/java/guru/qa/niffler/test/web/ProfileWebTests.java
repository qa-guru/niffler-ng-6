package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.CategoryExtension;
import guru.qa.niffler.jupiter.extension.CreateNewUserExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.ProfilePage;
import guru.qa.niffler.page.auth.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.util.Objects;

@ExtendWith({
        BrowserExtension.class,
        CreateNewUserExtension.class,
        CategoryExtension.class
})
class ProfileWebTests {

    static final Faker FAKE = new Faker();
    static final String LOGIN_PAGE_URL = Config.getInstance().authUrl() + "login";
    final ProfilePage profile = new ProfilePage();

    @Test
    void canAddNameTest(@CreateNewUser UserJson user) {

        var name = FAKE.name().fullName();

        Selenide.open(LOGIN_PAGE_URL, LoginPage.class)
                .login(user.getUsername(), user.getPassword())
                .getHeader()
                .openUserMenu()
                .goToProfilePage()
                .setName(name);

        Selenide.refresh();
        profile.shouldHaveName(name);

    }

    @Test
    void canUploadAvatarTest(@CreateNewUser UserJson user) {

        var file = new File(Objects.requireNonNull(getClass().getClassLoader().getResource("img/cat.jpeg")).getFile());

        Selenide.open(LOGIN_PAGE_URL, LoginPage.class)
                .login(user.getUsername(), user.getPassword())
                .getHeader()
                .openUserMenu()
                .goToProfilePage()
                .uploadAvatar(file)
                .save();

        Selenide.refresh();
        profile.shouldHaveImage();

    }

    @Test
    void canCreateNewCategoryTest(@CreateNewUser UserJson user) {

        var categoryName = FAKE.company().industry();

        Selenide.open(LOGIN_PAGE_URL, LoginPage.class)
                .login(user.getUsername(), user.getPassword())
                .getHeader()
                .openUserMenu()
                .goToProfilePage()
                .addNewCategory(categoryName);

        Selenide.refresh();
        profile.shouldBeActiveCategory(categoryName);

    }

    @Test
    void canNotCreateCategoryWithExistsNameTest(@CreateNewUser(categories = @Category) UserJson user) {

        var categoryName = user.getTestData().getCategories().getFirst().getName();

        Selenide.open(LOGIN_PAGE_URL, LoginPage.class)
                .login(user.getUsername(), user.getPassword())
                .getHeader()
                .openUserMenu()
                .goToProfilePage()
                .addNewCategory(categoryName)
                .shouldHaveMessageAlert("Error while adding category " + categoryName + ": Cannot save duplicates");

    }

    @Test
    void canEditCategoryNameTest(@CreateNewUser(categories = @Category) UserJson user) {

        var newCategoryName = FAKE.company().industry();

        Selenide.open(LOGIN_PAGE_URL, LoginPage.class)
                .login(user.getUsername(), user.getPassword())
                .getHeader()
                .openUserMenu()
                .goToProfilePage()
                .editCategoryName(user.getTestData().getCategories().getFirst().getName(), newCategoryName);

        Selenide.refresh();
        profile.shouldCategoryExists(newCategoryName);

    }

    @Test
    void canSetCategoryArchivedTest(@CreateNewUser(categories = @Category) UserJson user) {

        var categoryName = user.getTestData().getCategories().getFirst().getName();

        Selenide.open(LOGIN_PAGE_URL, LoginPage.class)
                .login(user.getUsername(), user.getPassword())
                .getHeader()
                .openUserMenu()
                .goToProfilePage()
                .setCategoryArchive(categoryName);

        Selenide.refresh();
        profile.showArchivedCategories()
                .shouldBeArchiveCategory(categoryName);

    }

    @Test
    void canSetCategoryUnarchivedTest(@CreateNewUser(categories = @Category(isArchived = true)) UserJson user) {

        var categoryName = user.getTestData().getCategories().getFirst().getName();

        Selenide.open(LOGIN_PAGE_URL, LoginPage.class)
                .login(user.getUsername(), user.getPassword())
                .getHeader()
                .openUserMenu()
                .goToProfilePage()
                .showArchivedCategories()
                .setCategoryActive(categoryName);

        Selenide.refresh();
        profile.shouldBeActiveCategory(categoryName);

    }

}