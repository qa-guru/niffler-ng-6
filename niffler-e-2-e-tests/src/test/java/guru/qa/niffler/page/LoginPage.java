package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
  private final SelenideElement usernameInput = $("input[name='username']").as("Поле ввода UserName");
  private final SelenideElement passwordInput = $("input[name='password']").as("Поле ввода Password");
  private final SelenideElement submitBtn = $("button[type='submit']").as("Кнопка 'Log in'");
  private final SelenideElement signInBtn = $("a[class='form__register']").as("Кнопка 'Create new account'");

  @Step("Заполнить поле 'Username'")
  public RegisterPage setUserName(String userName) {
    usernameInput.setValue(userName);
    return new RegisterPage();
  }

  @Step("Заполнить поле 'Password'")
  public RegisterPage setPassword(String password) {
    passwordInput.setValue(password);
    return new RegisterPage();
  }

  @Step("Нажать на кнопку 'Log in'")
  public RegisterPage submitRegistration() {
    submitBtn.click();
    return new RegisterPage();
  }

  @Step("Перейти на страницу регистрации нового пользователя")
  public RegisterPage goToRegistrationPage() {
    signInBtn.click();
    return new RegisterPage();
  }

  @Step("Выполнить вход в систему по username = {userName}")
  public MainPage doLogin(String userName, String password) {
    setUserName(userName);
    setPassword(password);
    submitRegistration();
    return new MainPage();
  }
}