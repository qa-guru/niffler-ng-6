package guru.qa.niffler.page.page.spending;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.page.component.CalendarComponent;
import guru.qa.niffler.page.page.MainPage;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Keys;

import javax.annotation.ParametersAreNonnullByDefault;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

@Slf4j
@NoArgsConstructor
@ParametersAreNonnullByDefault
public class AddNewSpendingPage extends SpendingPage<AddNewSpendingPage> {

    public AddNewSpendingPage(boolean checkPageElementVisible) {
        super(checkPageElementVisible);
    }

    private final SelenideElement openCalendarButton = $x("//button[./*[@alt='Calendar']]").as("['Date' calendar button]"),
            dateLabel = $("label[for='date']").as("['Date' label]"),
            dateInput = $("input[name='date']").as("['Date' input]");

    private final CalendarComponent calendarComponent = new CalendarComponent(
            $("div[class*=MuiDateCalendar-root]").as("Calendar form")
    );

    @Step("Create new spending")
    public MainPage createNewSpending(SpendJson spend) {
        fillSpendingData(spend);
        return submit();
    }

    @Step("Create new spending")
    public MainPage createNewSpendingWithCalendar(SpendJson spend) {
        fillSpendingDataWithCalendar(spend);
        return submit();
    }

    @Override
    public AddNewSpendingPage setDate(Date date) {
        var dateVal = new SimpleDateFormat("MM/dd/yyyy").format(date);
        log.info("Set date: [{}]", dateVal);
        Allure.step("Set date = [" + dateVal + "]", () -> {
            dateInput.click();
            dateInput.sendKeys(Keys.CONTROL + "a");
            dateInput.sendKeys(Keys.BACK_SPACE);
            dateInput.sendKeys(dateVal);
        });
        return this;
    }

    public AddNewSpendingPage selectDateFromCalendar(Date date) {
        openCalendarButton.click();
        calendarComponent.selectDateInCalendar(date);
        return this;
    }

    @Override
    public AddNewSpendingPage shouldHaveDate(Date date) {
        var dateValue = new SimpleDateFormat("MM/dd/yyyy").format(date);
        log.info("Calendar input should have value: [{}]", dateValue);
        Allure.step("Should have date = [" + dateValue + "] in calendar input", () -> {
            dateInput.shouldHave(value(dateValue));
        });
        return this;
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