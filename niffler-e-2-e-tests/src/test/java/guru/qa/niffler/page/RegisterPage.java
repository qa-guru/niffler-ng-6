package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;


public class RegisterPage {
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement passwordSubmitInput = $("#passwordSubmit");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement singInButton = $("a[class='form_sign-in']");
    private final SelenideElement msgUsernameAlreadyExist = $(withText("already exists"));
    private final SelenideElement msgPasswordsShouldBeEqual = $(withText("Passwords should be equal"));


    public RegisterPage setUsername(String username){
        usernameInput.setValue(username);
        return new RegisterPage();
    }
    public RegisterPage setPassword(String password){
        passwordInput.setValue(password);
        return new RegisterPage();
    }
    public RegisterPage setPasswordSubmit(String password){
        passwordSubmitInput.setValue(password);
        return new RegisterPage();
    }
    public LoginPage submitRegistration(){
        submitButton.click();
        return new LoginPage();
    }

    public LoginPage createUser(String username, String password){
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        passwordSubmitInput.setValue(password);
        submitButton.click();
        singInButton.click();
        return new LoginPage();
    }

    public  Boolean checkMsgUserAlreadyExistIsDisplayed(){
        return  msgUsernameAlreadyExist.isDisplayed();
    }

    public  Boolean checkPasswordsShouldBeEqual (){
        return  msgPasswordsShouldBeEqual.isDisplayed();
    }
}
