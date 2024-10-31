package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.ex.InvalidDateException;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static java.util.Calendar.*;

@Slf4j
public class CalendarWebService {

    private static final Date MIN_DATE = new GregorianCalendar(1970, JANUARY, 1).getTime(),
            MAX_DATE = new GregorianCalendar(2099, DECEMBER, 31, 23, 59, 59).getTime();

    private final SelenideElement
            calendarButton = $("img[alt='Calendar']").parent().as("Calendar button"),
            calendarForm = $("div[class*=MuiDateCalendar-root]").as("Calendar form"),
            dateInput = $("input[name='date']").as("'Date' input"),
            calendarTypeSwitchButton = calendarForm.$("[data-testid='ArrowDropDownIcon']").parent().as("Calendar type switcher button"),
            moveBackButton = calendarForm.$("button[aria-label='Previous month']").as("Previous month button"),
            moveForwardButton = calendarForm.$("button[aria-label='Next month']").as("Next month button"),
            calendarHeader = calendarForm.$("[class*='MuiPickersCalendarHeader-label ']").as("Calendar header"),
            yearsListContainer = calendarForm.$("div[class*='MuiYearCalendar']").as("Calendar years container");
    private final ElementsCollection yearsList = yearsListContainer.$$("button").as("Calendar years list"),
            daysList = calendarForm.$$("[class*='monthContainer'] button").as("Calendar days list");

    private static Calendar getCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public void pickDate(@Nonnull Date date) {

        validateDate(date);
        Calendar calendar = getCalendar(date);

        log.info("Pick date in calendar: {}/{}/{}", calendar.get(YEAR), calendar.get(MONTH), calendar.get(DAY_OF_MONTH));
        Allure.step("Pick date in calendar: [%s/%s/%s]".formatted(calendar.get(YEAR), calendar.get(MONTH), calendar.get(DAY_OF_MONTH)), () -> {
            openCalendar();
            selectYear(calendar.get(Calendar.YEAR));
            selectMonth(calendar.get(Calendar.MONTH));
            selectDay(calendar.get(Calendar.DAY_OF_MONTH));
            closeCalendar();
        });

    }

    @Step("Should have date in calendar input = ")
    public void calendarInputShouldHaveDate(@Nonnull Date date) {
        var dateValue = new SimpleDateFormat("MM/dd/yyyy").format(date);
        log.info("Calendar input should have value: [{}]", dateValue);
        Allure.step("Should have date = [" + dateValue + "] in calendar input", () -> {
            dateInput.shouldHave(value(dateValue));
        });

    }

    private void validateDate(@Nonnull Date date) {
        if (date.before(MIN_DATE) && date.after(MAX_DATE))
            throw new InvalidDateException("Available dates from [1970/01/01] to [2099/12/31]");
    }

    private CalendarType getCalendarType() {
        return Objects.requireNonNull(calendarTypeSwitchButton.getAttribute("aria-label")).contains("switch to year view")
                ? CalendarType.CALENDAR
                : CalendarType.YEAR;
    }

    private void switchCalendarType(CalendarType calendarType) {
        if (calendarType != getCalendarType()) {
            log.info("Switching calendar to: {}", calendarType);
            Allure.step("Change calendar type to [" + calendarType + "]", () -> {
                calendarTypeSwitchButton.click();
                // INFO: wait for switch calendar animation ends
                SelenideElement containerIdentifierElement = (calendarType == CalendarType.CALENDAR)
                        ? moveForwardButton
                        : yearsListContainer;
                containerIdentifierElement.shouldBe(visible);
            });
        }
    }

    @Step("Open calendar")
    private void openCalendar() {
        if (!calendarForm.is(visible, Duration.ofSeconds(2))) {
            calendarButton.shouldBe(visible).click();
        }
    }

    @Step("Close calendar")
    private void closeCalendar() {
        if (calendarForm.is(exist, Duration.ofSeconds(2)))
            calendarForm.pressEscape();
    }

    private int getCalendarYear() {
        return Integer.parseInt(calendarHeader.getText().split(" ")[1]);
    }

    @Step("Select year = [{}]")
    private void selectYear(int year) {
        switchCalendarType(CalendarType.YEAR);
        yearsList.find(text(String.valueOf(year))).scrollIntoView(false).click();
    }

    private int getCalendarMonth() {
        return Month.valueOf(calendarHeader.getText().split(" ")[0]).getValue();
    }

    @Step("Select month = [{}]")
    private void selectMonth(int month) {

        switchCalendarType(CalendarType.CALENDAR);
        var delta = month - getCalendarMonth();

        if (delta != 0) {

            SelenideElement moveElement = (delta > 0) ? moveForwardButton : moveBackButton;
            delta = Math.abs(delta);

            for (int steps = 0; steps < delta; steps++)
                moveElement.click();

            log.info("Selected month with number = [{}]", month);
            return;
        }
        log.info("Month with number = [{}] is already selected", month);

    }

    @Step("Select day = [{}]")
    private void selectDay(int day) {
        daysList.find(attribute("aria-colindex", String.valueOf(day))).click();
    }

    public enum CalendarType {
        CALENDAR, YEAR
    }

}