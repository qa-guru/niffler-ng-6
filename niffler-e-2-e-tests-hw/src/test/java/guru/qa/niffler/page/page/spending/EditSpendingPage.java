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

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

@Slf4j
@NoArgsConstructor
@ParametersAreNonnullByDefault
public class EditSpendingPage extends SpendingPage<EditSpendingPage> {

    private final SelenideElement openCalendarButton = $x("//button[./*[@alt='Calendar']]").as("['Date' calendar button]"),
            dateLabel = $("label[for='date']").as("['Date' label]"),
            dateInput = $("input[name='date']").as("['Date' input]");
    private final CalendarComponent calendarComponent = new CalendarComponent(
            $("div[class*=MuiDateCalendar-root]").as("Calendar form")
    );

    public EditSpendingPage(boolean checkPageElementVisible) {
        super(checkPageElementVisible);
    }

    public MainPage editSpending(SpendJson spending) {
        log.info("Edit spending: {}", spending);
        fillSpendingData(spending);
        return submit();
    }

    @Step("Edit spending to = [{spend}]")
    public MainPage editSpendingWithCalendar(SpendJson spend) {
        fillSpendingDataWithCalendar(spend);
        return submit();
    }

    @Override
    public EditSpendingPage setDate(Date date) {
        var dateVal = new SimpleDateFormat("MM/dd/yyyy").format(date);
        log.info("Set date: [{}]", dateVal);
        Allure.step("Set date = [" + dateVal + "]", () -> {
            dateInput.shouldBe(clickable).click();
            dateInput.sendKeys(Keys.CONTROL + "a");
            dateInput.sendKeys(Keys.BACK_SPACE);
            dateInput.sendKeys(dateVal);
        });
        return this;
    }

    public EditSpendingPage selectDateFromCalendar(Date date) {
        log.info("Pick date from calendar: [{}]", date);
        openCalendarButton.shouldBe(clickable).click();
        calendarComponent.selectDateInCalendar(date);
        return this;
    }

    @Override
    public EditSpendingPage shouldHaveDate(Date date) {
        var dateValue = new SimpleDateFormat("MM/dd/yyyy").format(date);
        log.info("Calendar input should have date: [{}]", dateValue);
        Allure.step("Should have date = [" + dateValue + "] in calendar input", () -> {
            dateInput.shouldHave(value(dateValue));
        });
        return this;
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
        dateLabel.shouldBe(visible);
        dateInput.shouldBe(visible);
        openCalendarButton.shouldBe(visible);
        saveButton.shouldHave(exactText("Save changes"));
        return this;
    }

}