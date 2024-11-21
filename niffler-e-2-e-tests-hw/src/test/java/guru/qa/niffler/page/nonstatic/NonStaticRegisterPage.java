package guru.qa.niffler.page.nonstatic;

import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import guru.qa.niffler.model.rest.UserJson;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.*;

@Slf4j
@ParametersAreNonnullByDefault
public class NonStaticRegisterPage extends NonStaticBasePage<NonStaticRegisterPage> {

    private final SelenideElement title = driver.$("h1").as("[Registration page title]");
    private final SelenideElement usernameInput = driver.$("#username").as("Username input");
    private final SelenideElement passwordInput = driver.$("#password").as("Password input");
    private final SelenideElement showPasswordButton = driver.$("#passwordBtn").as("Show password button");
    private final SelenideElement passwordConfirmationInput = driver.$("#passwordSubmit").as("Submit button");
    private final SelenideElement showPasswordConfirmationButton = driver.$("#passwordSubmitBtn").as("Submit button");
    private final SelenideElement submitButton = driver.$("button[type='submit']").as("Sign up button");
    private final SelenideElement goToLoginPageLink = driver.$("[href$='/main']").as("Log in link");


    public NonStaticRegisterPage() {
        super((SelenideDriver) WebDriverRunner.getWebDriver());
    }

    public NonStaticRegisterPage(SelenideDriver driver) {
        super(driver);
    }

    @Step("Sign up user = [{user.username}]")
    public NonStaticConfirmRegistrationPage signUpSuccess(UserJson user) {
        log.info("Sign up user by: username = [{}], password = [{}], passwordConfirmation = [{}]",
                user.getUsername(), user.getPassword(), user.getPasswordConfirmation());
        fillUsername(user.getUsername());
        fillPassword(user.getPassword());
        fillPasswordConfirmation(user.getPasswordConfirmation());
        submit();
        return driver == null
                ? new NonStaticConfirmRegistrationPage()
                : new NonStaticConfirmRegistrationPage(driver);
    }

    @Step("Sign up user = [{user.username}]")
    public void signInFailed(UserJson user) {
        log.info("Sign up [FAILED] user by: username = [{}], password = [{}], passwordConfirmation = [{}]",
                user.getUsername(), user.getPassword(), user.getPasswordConfirmation());
        fillUsername(user.getUsername());
        fillPassword(user.getPassword());
        fillPasswordConfirmation(user.getPasswordConfirmation());
        submit();
    }

    @Step("Fill username = [{}]")
    public void fillUsername(String username) {
        usernameInput.setValue(username);
    }

    @Step("Fill password = [{}]")
    public void fillPassword(String password) {
        passwordInput.setValue(password);
    }

    @Step("Fill password confirmation = [{}]")
    public void fillPasswordConfirmation(String passwordConfirmation) {
        passwordConfirmationInput.setValue(passwordConfirmation);
    }

    public NonStaticRegisterPage showPassword(boolean status) {
        if (status != showPasswordButton.has(cssClass("form__password-button_active"))) {
            log.info("Set password text visible = [{}]", status);
            Allure.step("Set show password status = [" + status + "]", () ->
                    showPasswordButton.shouldBe(clickable).click()
            );
        }
        return this;
    }

    public NonStaticRegisterPage showPasswordConfirmation(boolean status) {
        if (status != showPasswordConfirmationButton.has(cssClass("form__password-button_active"))) {
            log.info("Set password confirmation text visible = [{}]", status);
            Allure.step("Set show password confirmation status = [" + status + "]", () ->
                    showPasswordConfirmationButton.shouldBe(clickable).click()
            );
        }
        return this;
    }

    @Step("Submit sign up")
    private void submit() {
        log.info("Submit registration");
        submitButton.shouldBe(clickable).click();
    }

    @Step("Should visible username error with text = [{}]")
    public NonStaticRegisterPage assertUsernameHasError(String error) {
        usernameInput.parent().$(".form__error").as("Username error text").shouldHave(text(error));
        return this;
    }

    @Step("Should visible password error with text = [{}]")
    public NonStaticRegisterPage assertPasswordHasError(String error) {
        passwordInput.parent().$(".form__error").as("Password error text").shouldHave(text(error));
        return this;
    }

    @Step("Should visible password confirmation error with text = [{}]")
    public NonStaticRegisterPage assertPasswordConfirmationHasError(String error) {
        passwordConfirmationInput.parent().$(".form__error").as("Password confirmation error text").shouldHave(text(error));
        return this;
    }

    @Override
    public NonStaticRegisterPage shouldVisiblePageElement() {
        log.info("Assert registration page element is visible");
        title.shouldBe(visible);
        title.shouldHave(text("Sign up"));
        return this;
    }

    @Override
    @Step("Should visible 'Sign up' page")
    public NonStaticRegisterPage shouldVisiblePageElements() {

        log.info("Assert registration page elements are visible");

        usernameInput.shouldBe(visible);
        passwordInput.shouldBe(visible);
        passwordConfirmationInput.shouldBe(visible);
        submitButton.shouldBe(visible);
        goToLoginPageLink.shouldBe(visible);

        return this;

    }

}