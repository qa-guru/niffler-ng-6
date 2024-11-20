package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.utils.RandomDataUtils.randomCategoryName;
import static guru.qa.niffler.utils.RandomDataUtils.randomName;

@WebTest
public class ProfileTest {

  @User(
      categories = @Category(
          archived = true
      )
  )
  @Test
  void archivedCategoryShouldPresentInCategoriesList(UserJson user) {
    final String categoryName = user.testData().categoryDescriptions()[0];

    Selenide.open(LoginPage.URL, LoginPage.class)
        .fillLoginPage(user.username(), user.testData().password())
        .submit(new MainPage())
        .checkThatPageLoaded();

    Selenide.open(ProfilePage.URL, ProfilePage.class)
        .checkArchivedCategoryExists(categoryName);
  }

  @User(
      categories = @Category(
          archived = false
      )
  )
  @Test
  void activeCategoryShouldPresentInCategoriesList(UserJson user) {
    final String categoryName = user.testData().categoryDescriptions()[0];

    Selenide.open(LoginPage.URL, LoginPage.class)
        .fillLoginPage(user.username(), user.testData().password())
        .submit(new MainPage())
        .checkThatPageLoaded();

    Selenide.open(ProfilePage.URL, ProfilePage.class)
        .checkCategoryExists(categoryName);
  }

  @User
  @ApiLogin
  @Test
  void shouldUpdateProfileWithAllFieldsSet() {
    final String newName = randomName();

    ProfilePage profilePage = Selenide.open(ProfilePage.URL, ProfilePage.class)
        .uploadPhotoFromClasspath("img/cat.jpeg")
        .setName(newName)
        .submitProfile()
        .checkAlertMessage("Profile successfully updated");

    Selenide.refresh();

    profilePage.checkName(newName)
        .checkPhotoExist();
  }

  @User
  @Test
  void shouldUpdateProfileWithOnlyRequiredFields(UserJson user) {
    final String newName = randomName();

    ProfilePage profilePage = Selenide.open(LoginPage.URL, LoginPage.class)
        .fillLoginPage(user.username(), user.testData().password())
        .submit(new MainPage())
        .checkThatPageLoaded()
        .getHeader()
        .toProfilePage()
        .setName(newName)
        .submitProfile()
        .checkAlertMessage("Profile successfully updated");

    Selenide.refresh();

    profilePage.checkName(newName);
  }

  @User
  @Test
  void shouldAddNewCategory(UserJson user) {
    String newCategory = randomCategoryName();

    Selenide.open(LoginPage.URL, LoginPage.class)
        .fillLoginPage(user.username(), user.testData().password())
        .submit(new MainPage())
        .checkThatPageLoaded()
        .getHeader()
        .toProfilePage()
        .addCategory(newCategory)
        .checkAlertMessage("You've added new category:")
        .checkCategoryExists(newCategory);
  }

  @User(
      categories = {
          @Category(name = "Food"),
          @Category(name = "Bars"),
          @Category(name = "Clothes"),
          @Category(name = "Friends"),
          @Category(name = "Music"),
          @Category(name = "Sports"),
          @Category(name = "Walks"),
          @Category(name = "Books")
      }
  )
  @Test
  void shouldForbidAddingMoreThat8Categories(UserJson user) {
    Selenide.open(LoginPage.URL, LoginPage.class)
        .fillLoginPage(user.username(), user.testData().password())
        .submit(new MainPage())
        .checkThatPageLoaded()
        .getHeader()
        .toProfilePage()
        .checkThatCategoryInputDisabled();
  }
}
