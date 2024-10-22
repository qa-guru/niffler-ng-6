package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$;

public class SearchField {
    private final SelenideElement searchField = $("input[type='text']");

    @Step("Поиск по значению: {value}")
    public SearchField search(String value) {
        searchField.sendKeys(value);
        searchField.sendKeys(Keys.ENTER);
        return this;
    }

    @Step("Очистить строку поиска")
    public SearchField clearIfNotEmpty() {
        searchField.clear();
        return this;
    }
}
