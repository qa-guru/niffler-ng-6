package guru.qa.niffler.page.spending;

import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.MainPage;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import static com.codeborne.selenide.Condition.exactText;

@Slf4j
public class EditSpendingPage extends SpendingPage<EditSpendingPage> {

    public MainPage editSpending(@NonNull SpendJson spending) {
        log.info("Edit spending data to: {}", spending);
        fillSpendingData(spending);
        submit();
        return new MainPage();
    }

    @Override
    public EditSpendingPage shouldVisiblePageElements() {
        shouldVisibleSpendingPageElements();
        title.shouldHave(exactText("Edit spending"));
        saveButton.shouldHave(exactText("Save changes"));
        return this;
    }

}
