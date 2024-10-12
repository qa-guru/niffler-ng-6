package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

  private final SelenideElement usernameInput = $("input[name='username']");
  private final SelenideElement passwordInput = $("input[name='password']");
  private final SelenideElement submitButton = $("button[type='submit']");
  private final SelenideElement registerButton = $("a[href='/register']");
  private final SelenideElement errorContainer = $(".form__error");

  public RegisterPage doRegister() {
    registerButton.click();
    return new RegisterPage();
  }

  public MainPage successLogin(String username, String password) {
    login(username, password);
    return new MainPage();
  }

  public void login(String username, String password) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    submitButton.click();
  }

  public LoginPage checkError(String error) {
    errorContainer.shouldHave(text(error));
    return this;
  }
}
