package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class SignInPage {
  private final SelenideElement usernameInput = $("input[name='username']");
  private final SelenideElement passwordInput = $("input[name='password']");
  private final SelenideElement submitButton = $("button[type='submit']");
  private final SelenideElement signUpButton =$(".form__register");
  private final SelenideElement errorMessage = $("p.form__error");

  public MainPage signIn(String username, String password) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    submitButton.click();
    return new MainPage();
  }

  public SignUpPage clickSignUpButton() {
    signUpButton.click();
    return new SignUpPage();
  }

  public boolean isBadCredentialsErrorMessageDisplayed() {
    String expectedError = "Bad credentials";
    return errorMessage.shouldHave(Condition.text(expectedError)).isDisplayed();
  }
}
