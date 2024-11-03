package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;

public class SearchField {

    private final SelenideElement self;

    public SearchField(SelenideElement self) {
        this.self = self;
    }

    public SearchField search(String query) {
        self.setValue(query).pressEnter();
        return this;
    }

    public SearchField clear() {
        self.$("#input-clear").click();
        return this;
    }
}
