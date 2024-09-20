package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement createNewAccountLink = $(byText("Create new account"));
    private final SelenideElement errorText = $(".form__error");

    public MainPage login(String username, String password) {
        setUsername(username);
        setPassword(password);
        clickLogInButton();
        return new MainPage();
    }

    public LoginPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    public LoginPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }
    
    public MainPage clickLogInButton() {
        submitButton.click();
        return new MainPage();
    }

    public RegisterPage clickCreateNewAccount() {
        createNewAccountLink.click();
        return new RegisterPage();
    }

    public LoginPage checkMessageThatWasInputBadCredentials(String badCredentialText) {
        errorText.shouldHave(text(badCredentialText)).shouldBe(visible);
        return this;
    }
}
