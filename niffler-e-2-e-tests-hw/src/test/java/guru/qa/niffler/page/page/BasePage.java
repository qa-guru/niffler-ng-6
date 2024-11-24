package guru.qa.niffler.page.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import io.qameta.allure.Step;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

@Slf4j
@NoArgsConstructor
@ParametersAreNonnullByDefault
public abstract class BasePage<T> {

    protected static final Config CFG = Config.getInstance();
    protected static final String BASE_URL = CFG.frontUrl();
    protected static final String AUTH_URL = CFG.authUrl();

    protected BasePage(boolean assertPageElementsOnStart) {
        if (assertPageElementsOnStart)
            try {
                shouldVisiblePageElement();
            } catch (Exception ignored) {
                // NOP
            }
    }

    public abstract T shouldVisiblePageElement();    private final SelenideElement alert = $("div[role='alert']").as("[Alert form]"),
            closeAlert = alert.$("button").as("[Close alert button]"),
            icon = alert.$("div[class^=MuiAlert-icon] svg").as("[Alert icon]"),
            message = alert.$("div[class^=MuiAlert-message] div").as("[Alert message]");

    public abstract T shouldVisiblePageElements();

    @SuppressWarnings("unchecked")
    @Step("Alert should have message = [{text}]")
    public T shouldHaveMessageAlert(String text) {
        log.info("Alert should have message: [{}]", message);
        message.shouldBe(visible).shouldHave(exactText(text));
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Step("Alert should have status = [success]")
    public T shouldBeSuccessAlert() {
        log.info("Alert should have status: [success]");
        icon.shouldBe(visible).shouldHave(attribute("data-testid", "SuccessOutlinedIcon"));
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Step("Alert should have status = [info]")
    public T shouldBeInfoAlert() {
        log.info("Alert should have status: [info]");
        icon.shouldBe(visible).shouldHave(attribute("data-testid", "InfoOutlinedIcon"));
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Step("Alert should have status = [error]")
    public T shouldBeErrorAlert() {
        log.info("Alert should have status: [error]");
        icon.shouldBe(visible).shouldHave(attribute("data-testid", "ErrorOutlineIcon"));
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Step("Close alert")
    public T closeAlert() {
        log.info("Close alert");
        closeAlert.shouldBe(clickable).click();
        return (T) this;
    }




}