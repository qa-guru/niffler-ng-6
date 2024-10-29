package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import io.qameta.allure.Step;
import lombok.Getter;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public abstract class BasePage<T extends BasePage<?>> {
    protected final SelenideElement alert = $x("//div[contains(@class,'MuiTypography-root MuiTypography-body1')]");
    @Getter
    protected final Header<T> header;


    @SuppressWarnings("unchecked")
    @Step("Проверка всплывающего сообщения: {message}")
    public T checkAlert(String message) {
        alert.shouldHave(text(message));
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public BasePage() {
        this.header = new Header<>($("#root header"), (T) this);
    }
}