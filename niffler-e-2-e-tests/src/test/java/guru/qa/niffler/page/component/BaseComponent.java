package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.BasePage;
import lombok.Getter;

public class BaseComponent<T extends BasePage<?>> {  // Указываем, что T должен быть подклассом BasePage

    protected final SelenideElement self;
    @Getter
    protected final T page;  // Страница, к которой компонент привязан

    public BaseComponent(SelenideElement self, T page) {
        this.self = self;
        this.page = page;
    }
}