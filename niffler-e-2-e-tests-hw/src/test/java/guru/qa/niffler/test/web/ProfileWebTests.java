package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.CreateNewUser;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.page.ProfilePage;
import org.junit.jupiter.api.Test;

@WebTest
class ProfileWebTests {

    static final Faker FAKE = new Faker();
    final ProfilePage profile = new ProfilePage();

    @Test
    void canAddNameTest(
            @ApiLogin(setupBrowser = true)
            @CreateNewUser
            UserJson user
    ) {

        var name = FAKE.name().fullName();

        Selenide.open(ProfilePage.URL, ProfilePage.class)
                .setName(name);

        Selenide.refresh();
        profile.shouldHaveName(name);

    }

    @Test
    void canUploadAvatarTest(
            @ApiLogin(setupBrowser = true)
            @CreateNewUser
            UserJson user
    ) {

        Selenide.open(ProfilePage.URL, ProfilePage.class)
                .uploadAvatar("img/cat.jpeg")
                .save();

        Selenide.refresh();
        profile.shouldHaveImage();

    }

    @Test
    void canCreateNewCategoryTest(
            @ApiLogin(setupBrowser = true)
            @CreateNewUser
            UserJson user
    ) {

        var categoryName = FAKE.company().industry();

        Selenide.open(ProfilePage.URL, ProfilePage.class)
                .addNewCategory(categoryName);

        Selenide.refresh();
        profile.shouldBeActiveCategory(categoryName);

    }

    @Test
    void canNotCreateCategoryWithExistsNameTest(
            @ApiLogin(setupBrowser = true)
            @CreateNewUser(
                    categories = @Category
            )
            UserJson user
    ) {

        var categoryName = user.getTestData().getCategories().getFirst().getName();

        Selenide.open(ProfilePage.URL, ProfilePage.class)
                .addNewCategory(categoryName)
                .shouldBeErrorAlert()
                .shouldHaveMessageAlert("Error while adding category " + categoryName + ": Cannot save duplicates");

    }

    @Test
    void canEditCategoryNameTest(
            @ApiLogin(setupBrowser = true)
            @CreateNewUser(
                    categories = @Category
            )
            UserJson user
    ) {

        var newCategoryName = FAKE.company().industry();

        Selenide.open(ProfilePage.URL, ProfilePage.class)
                .editCategoryName(user.getTestData().getCategories().getFirst().getName(), newCategoryName);

        Selenide.refresh();
        profile.shouldCategoryExists(newCategoryName);

    }

    @Test
    void canSetCategoryArchivedTest(
            @ApiLogin
            @CreateNewUser(
                    categories = @Category
            )
            UserJson user
    ) {

        var categoryName = user.getTestData().getCategories().getFirst().getName();

        Selenide.open(ProfilePage.URL, ProfilePage.class)
                .setCategoryArchive(categoryName);

        Selenide.refresh();
        profile.showArchivedCategories()
                .shouldBeArchiveCategory(categoryName);

    }

    @Test
    void canSetCategoryUnarchivedTest(
            @ApiLogin(setupBrowser = true)
            @CreateNewUser(
                    categories = @Category(isArchived = true)
            )
            UserJson user
    ) {

        var categoryName = user.getTestData().getCategories().getFirst().getName();

        Selenide.open(ProfilePage.URL, ProfilePage.class)
                .showArchivedCategories()
                .setCategoryActive(categoryName);

        Selenide.refresh();
        profile.shouldBeActiveCategory(categoryName);

    }

}