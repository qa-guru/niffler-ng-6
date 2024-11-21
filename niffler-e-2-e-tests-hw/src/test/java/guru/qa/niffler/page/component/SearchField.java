package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.clickable;

@Slf4j
@ParametersAreNonnullByDefault
public class SearchField extends BaseComponent<SearchField> {

    SelenideElement input = self.$("input[aria-label='search']").as("[Text input]"),
            inputClearButton = self.$("#input-clear").as("[Text input clear button]");

    public SearchField(SelenideElement self) {
        super(self);
    }

    public void setValue(String text) {
        input.setValue(text).pressEnter();
    }

    public void clearByButton() {
        self.shouldBe(clickable).click();
        if (!input.getValue().isEmpty())
            inputClearButton.shouldBe(clickable).click();
    }

    public void clear() {
        input.clear();
    }

}
