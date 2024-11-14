package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

public class RegisterPage extends BasePage<RegisterPage> {

  public static final String URL = CFG.authUrl() + "register";

  private final SelenideElement usernameInput;
  private final SelenideElement passwordInput;
  private final SelenideElement passwordSubmitInput;
  private final SelenideElement submitButton;
  private final SelenideElement proceedLoginButton;
  private final SelenideElement errorContainer;

  public RegisterPage(SelenideDriver driver) {
    super(driver);
    this.usernameInput = driver.$("input[name='username']");
    this.passwordInput = driver.$("input[name='password']");
    this.passwordSubmitInput = driver.$("input[name='passwordSubmit']");
    this.submitButton = driver.$("button[type='submit']");
    this.proceedLoginButton = driver.$(".form_sign-in");
    this.errorContainer = driver.$(".form__error");
  }

  @Step("Fill register page with credentials: username: {0}, password: {1}, submit password: {2}")
  @Nonnull
  public RegisterPage fillRegisterPage(String login, String password, String passwordSubmit) {
    setUsername(login);
    setPassword(password);
    setPasswordSubmit(passwordSubmit);
    return this;
  }

  @Step("Set username: {0}")
  @Nonnull
  public RegisterPage setUsername(String username) {
    usernameInput.setValue(username);
    return this;
  }

  @Step("Set password: {0}")
  @Nonnull
  public RegisterPage setPassword(String password) {
    passwordInput.setValue(password);
    return this;
  }

  @Step("Confirm password: {0}")
  @Nonnull
  public RegisterPage setPasswordSubmit(String password) {
    passwordSubmitInput.setValue(password);
    return this;
  }

  @Step("Submit register")
  @Nonnull
  public LoginPage successSubmit() {
    submitButton.click();
    proceedLoginButton.click();
    return new LoginPage(driver);
  }

  @Step("Submit register")
  @Nonnull
  public RegisterPage errorSubmit() {
    submitButton.click();
    return this;
  }

  @Step("Check that page is loaded")
  @Override
  @Nonnull
  public RegisterPage checkThatPageLoaded() {
    usernameInput.should(visible);
    passwordInput.should(visible);
    passwordSubmitInput.should(visible);
    return this;
  }

  @Nonnull
  public RegisterPage checkAlertMessage(String errorMessage) {
    errorContainer.shouldHave(text(errorMessage));
    return this;
  }
}
