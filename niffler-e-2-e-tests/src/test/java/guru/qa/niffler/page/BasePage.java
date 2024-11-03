package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public abstract class BasePage<T extends  BasePage<?>> {

    protected final SelenideElement alert = $("");

    public T checkAlert(String message) {
        alert.shouldHave(text(message));
        return (T) this;
    }
}
