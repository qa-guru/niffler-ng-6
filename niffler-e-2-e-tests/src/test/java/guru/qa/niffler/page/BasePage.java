package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.component.Header;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public abstract class BasePage<T extends BasePage<?>> {

    protected final Header header = new Header();
    protected final SelenideElement alert;
    protected static final Config CFG = Config.getInstance();

    public BasePage(SelenideDriver driver) {
        this.alert = driver.$(".MuiSnackbar-root");
    }

    public BasePage() {
        this.alert = $(".MuiSnackbar-root");
    }


    public T checkAlert(String message) {
        alert.shouldHave(text(message));
        return (T) this;
    }


}
