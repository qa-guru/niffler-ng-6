package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
  private final SelenideElement usernameInput = $("input[name='username']");
  private final SelenideElement passwordInput = $("input[name='password']");
  private final SelenideElement submitButton = $("button[type='submit']");
  private final SelenideElement signUpButton = $(".form__register");
  private final SelenideElement errorMessage = $(".form__error");

  public MainPage login(String username, String password) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    submitButton.click();
    return new MainPage();
  }

  public RegisterPage clickSignUpButton(){
    signUpButton.click();
    return new RegisterPage();
  }

  public LoginPage checkLoginErrorMessage(){
    errorMessage.shouldHave(Condition.exactText("Неверные учетные данные пользователя"));
    usernameInput.shouldBe(Condition.visible);
    return this;
  }
}
