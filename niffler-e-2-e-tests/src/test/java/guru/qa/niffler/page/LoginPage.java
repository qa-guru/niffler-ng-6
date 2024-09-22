package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement submitBtn = $("button[type='submit']");

    public MainPage doLogin(String userName, String password) {
        usernameInput.setValue(userName);
        passwordInput.setValue(password);
        submitBtn.click();
        return new MainPage();
    }
}