package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.CategoryExtension;
import guru.qa.niffler.jupiter.extension.CreateNewUserExtension;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.UserModel;
import guru.qa.niffler.page.ProfilePage;
import guru.qa.niffler.page.auth.LoginPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.util.Objects;

@ExtendWith({
        BrowserExtension.class,
        CreateNewUserExtension.class,
        CategoryExtension.class
})
class ProfileTests {

    static final Faker FAKE = new Faker();
    static final String LOGIN_PAGE_URL = Config.getInstance().authUrl() + "login";

    final ProfilePage profile = new ProfilePage();

    @Test
    @CreateNewUser
    void canAddNameTest(UserModel user) {

        var name = FAKE.name().fullName();

        Selenide.open(LOGIN_PAGE_URL, LoginPage.class)
                .login(user.getUsername(), user.getPassword())
                .getHeader()
                .openUserMenu()
                .goToProfilePage()
                .setName(name);

        Selenide.refresh();
        Assertions.assertEquals(name, profile.getName());

    }

    @Test
    @CreateNewUser
    void canUploadAvatarTest(UserModel user) {

        var file = new File(Objects.requireNonNull(getClass().getClassLoader().getResource("img/cat.jpeg")).getFile());

        Selenide.open(LOGIN_PAGE_URL, LoginPage.class)
                .login(user.getUsername(), user.getPassword())
                .getHeader()
                .openUserMenu()
                .goToProfilePage()
                .uploadAvatar(file)
                .save();

        Selenide.refresh();
        profile.assertAvatarHasImage();

    }

    @Test
    @CreateNewUser
    void canCreateNewCategoryTest(UserModel user) {

        var categoryName = FAKE.company().industry();

        Selenide.open(LOGIN_PAGE_URL, LoginPage.class)
                .login(user.getUsername(), user.getPassword())
                .getHeader()
                .openUserMenu()
                .goToProfilePage()
                .addNewCategory(categoryName);

        Selenide.refresh();
        profile.assertCategoryExists(categoryName);

    }

    @Test
    @CreateNewUser
    @Category
    void canNotCreateCategoryWithExistsNameTest(UserModel user, CategoryJson category) {

        var categoryName = category.getName();

        Selenide.open(LOGIN_PAGE_URL, LoginPage.class)
                .login(user.getUsername(), user.getPassword())
                .getHeader()
                .openUserMenu()
                .goToProfilePage()
                .addNewCategory(categoryName)
                .assertAlertMessageHasText("Error while adding category " + categoryName + ": Cannot save duplicates");

    }

    @Test
    @CreateNewUser
    @Category
    void canChangeCategoryNameTest(UserModel user, CategoryJson category) {

        var newCategoryName = FAKE.company().industry();

        Selenide.open(LOGIN_PAGE_URL, LoginPage.class)
                .login(user.getUsername(), user.getPassword())
                .getHeader()
                .openUserMenu()
                .goToProfilePage()
                .changeCategoryName(category.getName(), newCategoryName);

        Selenide.refresh();
        profile.assertCategoryExists(newCategoryName);

    }

    @Test
    @CreateNewUser
    @Category
    void canSetCategoryArchivedTest(UserModel user, CategoryJson category) {

        var categoryName = category.getName();

        Selenide.open(LOGIN_PAGE_URL, LoginPage.class)
                .login(user.getUsername(), user.getPassword())
                .getHeader()
                .openUserMenu()
                .goToProfilePage()
                .changeCategoryStatus(categoryName, false)
                .toggleShowArchived(true);

        Selenide.refresh();
        profile.assertCategoryHasStatus(categoryName, false);

    }

    @Test
    @CreateNewUser
    @Category(isArchived = true)
    void canSetCategoryUnarchivedTest(UserModel user, CategoryJson category) {

        var categoryName = category.getName();

        Selenide.open(LOGIN_PAGE_URL, LoginPage.class)
                .login(user.getUsername(), user.getPassword())
                .getHeader()
                .openUserMenu()
                .goToProfilePage()
                .toggleShowArchived(true)
                .changeCategoryStatus(categoryName, true);

        Selenide.refresh();
        profile.assertCategoryHasStatus(categoryName, true);

    }

}
