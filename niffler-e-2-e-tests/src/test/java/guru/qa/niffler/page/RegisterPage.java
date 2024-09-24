package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class RegisterPage {
  private final SelenideElement usernameInput = $("input[id='username']").as("Поле ввода UserName");
  private final SelenideElement passwordInput = $("input[id='password']").as("Поле ввода Password");
  private final SelenideElement passwordSubmitInput = $("input[id='passwordSubmit']").as("Поле ввода 'Submit password'");
  private final SelenideElement submitBtn = $("button[type='submit']").as("Кнопка 'Sign Up'");
  private final SelenideElement signInBtn = $("a[class='form_sign-in']").as("Кнопка 'Sign In'");
  private final SelenideElement userExistErrorText = $x("//input[@id='username']/parent::label/span").as("Текст ошибки пол Username регистрации существующего пользователя");
  private final SelenideElement passwordErrorText = $x("//input[@id='password']/parent::label/span").as("Текст ошибки поля Password");
  private final SelenideElement submitPasswordErrorText = $x("//input[@id='passwordSubmit']/parent::label/span").as("Текст ошибки поля Password");

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

  @Step("Заполнить поле 'Submit password'")
  public RegisterPage setSubmitPassword(String password) {
    passwordSubmitInput.setValue(password);
    return new RegisterPage();
  }

  @Step("Нажать на кнопку 'Sign Up'")
  public RegisterPage submitRegistration() {
    submitBtn.click();
    return new RegisterPage();
  }

  @Step("Нажать на кнопку 'Sign In'")
  public LoginPage goToLoginPage() {
    signInBtn.click();
    return new LoginPage();
  }

  @Step("Проверка отображении текста об ошибке поля Username")
  public RegisterPage checkUsernameErrorText(String errorText) {
    userExistErrorText.shouldHave(Condition.text(errorText));
    return new RegisterPage();
  }

  @Step("Проверка отображении текста об ошибке поля Password")
  public RegisterPage checkPasswordErrorText(String errorText) {
    passwordErrorText.shouldHave(Condition.text(errorText));
    return new RegisterPage();
  }

  @Step("Проверка отображении текста об ошибке поля Submit password")
  public RegisterPage checkSubmitPasswordErrorText(String errorText) {
    submitPasswordErrorText.shouldHave(Condition.text(errorText));
    return new RegisterPage();
  }

  @Step("Заполнить поля нового пользователя с логином {userName}")
  public RegisterPage setNewUserDataField(String userName, String password, String submitPassword) {
    setUserName(userName);
    setPassword(password);
    setSubmitPassword(submitPassword);
    return new RegisterPage();
  }

  @Step("Зарегистрировать нового пользователя с логином {userName}")
  public LoginPage registerNewUser(String userName, String password) {
    setNewUserDataField(userName, password, password);
    submitRegistration();
    return goToLoginPage();
  }
}