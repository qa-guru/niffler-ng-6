package guru.qa.niffler.page.spending;

import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.MainPage;

import static com.codeborne.selenide.Condition.exactText;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddNewSpendingPage extends SpendingPage<AddNewSpendingPage> {

    @Override
    public AddNewSpendingPage assertPageElementsAreVisible() {
        assertSpendingPageElementsAreVisible();
        assertAll("Assert spending page is 'Edit spending' page", () -> {
            assertTrue(title.has(exactText("Add new spending")));
            assertTrue(saveButton.has(exactText("Add")));
        });
        return this;
    }

    public MainPage createNewSpending(SpendJson spend) {
        fillSpendingData(spend);
        return submit();
    }

}
