package guru.qa.niffler.page.spending;

import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.MainPage;
import lombok.NonNull;

import static com.codeborne.selenide.Condition.exactText;

public class AddNewSpendingPage extends SpendingPage<AddNewSpendingPage> {

    public MainPage createNewSpending(@NonNull SpendJson spend) {
        fillSpendingData(spend);
        return submit();
    }

    @Override
    public AddNewSpendingPage shouldVisiblePageElements() {
        shouldVisibleSpendingPageElements();
        title.shouldHave(exactText("Add new spending"));
        saveButton.shouldHave(exactText("Add"));
        return this;
    }
}
