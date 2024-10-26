package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.BasePage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.empty;

public class SearchField<T extends BasePage<?>> extends BaseComponent<T> {

    public SearchField(SelenideElement searchFieldElement, T page) {
        super(searchFieldElement, page);
    }

    @Step("Поиск по значению: {value}")
    public T search(String value) {
        self.setValue(value).pressEnter();
        return page;
    }

    @Step("Очистить строку поиска, если она не пустая")
    public SearchField<T> clearIfNotEmpty() {
        if (!self.shouldBe(empty).exists()) {
            self.clear();
        }
        return this;
    }
}