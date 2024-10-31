package guru.qa.niffler.page.spending;

import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.MainPage;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.exactText;

@Slf4j
@NoArgsConstructor
public class AddNewSpendingPage extends SpendingPage<AddNewSpendingPage> {

    public AddNewSpendingPage(boolean checkPageElementVisible) {
        super(checkPageElementVisible);
    }

    public MainPage createNewSpending(@Nonnull SpendJson spend) {
        fillSpendingData(spend);
        return submit();
    }

    @Override
    public AddNewSpendingPage shouldVisiblePageElement() {
        log.info("Assert 'Add new spending' page element visible on start up");
        saveButton.shouldHave(exactText("Add"));
        return this;
    }

    @Override
    public AddNewSpendingPage shouldVisiblePageElements() {
        log.info("Assert 'Add new spending' page are visible");
        shouldVisibleSpendingPageElements();
        title.shouldHave(exactText("Add new spending"));
        saveButton.shouldHave(exactText("Add"));
        return this;
    }

}