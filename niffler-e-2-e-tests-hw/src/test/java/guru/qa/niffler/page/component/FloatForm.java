package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.page.BasePage;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

@Slf4j
@ParametersAreNonnullByDefault
public class FloatForm extends BaseComponent<FloatForm> {

    private final SelenideElement title = self.$("h2").as("[Float form title]"),
            message = self.$("p").as("[Float form message]"),
            cancel = self.$("button:nth-child(1)").as("[Cancel button]"),
            submit = self.$("button:nth-child(2)").as("[Submit button]");

    public FloatForm() {
        super($(".MuiDialog-container").as("[Float form]"));
    }

    public FloatForm(@Nonnull SelenideElement self) {
        super(self);
    }

    @Step("Submit")
    public void submit() {
        submit.shouldBe(clickable).click();
    }

    @Step("Submit")
    public <T extends BasePage<T>> T submit(Class<T> page) {
        submit.shouldBe(clickable).click();
        return createPageInstance(page);
    }

    @Step("Cancel")
    public void cancel() {
        cancel.shouldBe(clickable).click();
    }

    @Step("Cancel")
    public <T extends BasePage<T>> T cancel(Class<T> page) {
        cancel.shouldBe(clickable).click();
        return createPageInstance(page);
    }

    @Step("Should have title = [{text}]")
    public FloatForm shouldHaveTitle(String text) {
        log.info("Should have title = [" + text + "]");
        title.shouldBe(visible).shouldHave(text(text));
        return this;
    }

    @Step("Should have message = [{text}]")
    public FloatForm shouldHaveMessage(String text) {
        log.info("Should have title = [" + text + "]");
        message.shouldBe(visible).shouldHave(text(text));
        return this;
    }

    private <T extends BasePage<T>> T createPageInstance(Class<T> page) {
        try {
            // Create a new instance of the specified page class
            return page.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Unable to create an instance of page class: " + page.getName(), e);
        }
    }

}
