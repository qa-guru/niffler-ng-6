package guru.qa.niffler.page.spending;

import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.MainPage;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.exactText;

@Slf4j
@NoArgsConstructor
public class EditSpendingPage extends SpendingPage<EditSpendingPage> {

    public EditSpendingPage(boolean checkPageElementVisible) {
        super(checkPageElementVisible);
    }

    public MainPage editSpending(@Nonnull SpendJson spending) {
        log.info("Edit spending data to: {}", spending);
        fillSpendingData(spending);
        submit();
        return new MainPage(true);
    }

    @Override
    public EditSpendingPage shouldVisiblePageElement() {
        log.info("Assert 'Edit spending' page element visible on start up");
        saveButton.shouldHave(exactText("Add"));
        return this;
    }

    @Override
    public EditSpendingPage shouldVisiblePageElements() {
        shouldVisibleSpendingPageElements();
        title.shouldHave(exactText("Edit spending"));
        saveButton.shouldHave(exactText("Save changes"));
        return this;
    }

}