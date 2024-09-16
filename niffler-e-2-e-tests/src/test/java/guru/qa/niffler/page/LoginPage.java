package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
  private final SelenideElement usernameInput = $("input[name='username']");
  private final SelenideElement passwordInput = $("input[name='password']");
  private final SelenideElement submitButton = $("button[type='submit']");

  public MainPage login(String username, String password) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    submitButton.click();
    return new MainPage();
  }

  private final SelenideElement createAccountButton = $(".form__register");

  public void openRegisterPage() {
    createAccountButton.click();
  }

  private final SelenideElement errorMessageText = $(".form__error");

  public LoginPage checkErrorAboutBadCredentialsIsDisplayed(String errorMessage) {
    errorMessageText.shouldBe(visible);
    return this;
  }

  public void checkUserStayOnLoginPageAfterLoginWithBadCredentials() {
    usernameInput.shouldBe(visible);
    passwordInput.shouldBe(visible);
    submitButton.shouldBe(visible);
  }
}
