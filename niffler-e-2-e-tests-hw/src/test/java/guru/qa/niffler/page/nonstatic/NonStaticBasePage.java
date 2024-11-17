package guru.qa.niffler.page.nonstatic;

import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.*;

@Slf4j
@ParametersAreNonnullByDefault
public abstract class NonStaticBasePage<T> {

    protected final SelenideDriver driver;

    private final SelenideElement alert;
    private final SelenideElement closeAlert;
    private final SelenideElement icon;
    private final SelenideElement message;

    protected NonStaticBasePage(SelenideDriver driver) {
        this.driver = driver;
        this.alert = driver.$("div[role='alert']").as("[Alert form]");
        this.closeAlert = alert.$("button").as("[Close alert button]");
        this.icon = alert.$("div[class^=MuiAlert-icon] svg").as("[Alert icon]");
        this.message = alert.$("div[class^=MuiAlert-message] div").as("[Alert message]");
    }

    public abstract T shouldVisiblePageElement();

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
        closeAlert.click();
        return (T) this;
    }

}