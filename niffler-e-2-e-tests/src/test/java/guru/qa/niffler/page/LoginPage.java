package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
  private final SelenideElement usernameInput = $("input[name='username']");
  private final SelenideElement passwordInput = $("input[name='password']");
  private final SelenideElement submitButton = $("button[type='submit']");
  private final SelenideElement createNewAccountButton = $("a.form__register");
  private final SelenideElement errorTitle = $(".form__error");

  public MainPage login(String username, String password) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    submitButton.click();
    return new MainPage();
  }

  public RegisterPage clickCreateAccountButton() {
    createNewAccountButton.click();
    return new RegisterPage();
  }

  public LoginPage shouldErrorTitle(String value) {
    errorTitle.shouldHave(text(value));
    return this;
  }
}
