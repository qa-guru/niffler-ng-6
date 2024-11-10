package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class RegisterPage {

  private final SelenideElement usernameInput = $("input[name='username']");
  private final SelenideElement passwordInput = $("input[name='password']");
  private final SelenideElement passwordSubmitInput = $("input[name='passwordSubmit']");
  private final SelenideElement submitButton = $("button[type='submit']");
  private final SelenideElement proceedLoginButton = $(".form_sign-in");
  private final SelenideElement errorContainer = $(".form__error");

  @Nonnull
  public RegisterPage fillRegisterPage(String login, String password, String passwordSubmit) {
    usernameInput.setValue(login);
    passwordInput.setValue(password);
    passwordSubmitInput.setValue(passwordSubmit);
    return this;
  }

  @Nonnull
  public LoginPage successSubmit() {
    submit();
    proceedLoginButton.click();
    return new LoginPage();
  }

  public void submit() {
    submitButton.click();
  }

  @Nonnull
  public RegisterPage checkAlertMessage(String errorMessage) {
    errorContainer.shouldHave(text(errorMessage));
    return this;
  }
}
