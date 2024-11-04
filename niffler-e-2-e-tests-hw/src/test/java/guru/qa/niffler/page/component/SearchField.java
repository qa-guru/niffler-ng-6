package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selenide.$;

@Slf4j
@ParametersAreNonnullByDefault
public class SearchField extends BaseComponent<SearchField> {

    SelenideElement input = $("input[aria-label='search']").as("[Text input]"),
            inputClearButton = self.$("#input-clear").as("[Text input clear button]");

    public SearchField(SelenideElement self) {
        super(self);
    }

    public void setValue(String text) {
        input.setValue(text).pressEnter();
    }

    public void clearByButton() {
        self.click();
        inputClearButton.click();
    }

    public void clear() {
        self.clear();
    }

}
