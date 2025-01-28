package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;

public class SearchField extends BaseComponent<SearchField> {

    public SearchField(SelenideElement self) {
        super(self);
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
