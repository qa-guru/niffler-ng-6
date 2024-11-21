package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.ex.InvalidDateException;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.ParametersAreNonnullByDefault;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

import static com.codeborne.selenide.Condition.*;
import static java.util.Calendar.DECEMBER;
import static java.util.Calendar.JANUARY;

@Slf4j
@ParametersAreNonnullByDefault
public class CalendarComponent extends BaseComponent<CalendarComponent> {

    private static final Date MIN_DATE = new GregorianCalendar(1970, JANUARY, 1).getTime(),
            MAX_DATE = new GregorianCalendar(2099, DECEMBER, 31, 23, 59, 59).getTime();

    private final SelenideElement
            calendarTypeSwitchButton = self.$("[data-testid='ArrowDropDownIcon']").parent().as("Calendar type switcher button"),
            moveBackButton = self.$("button[aria-label='Previous month']").as("Previous month button"),
            moveForwardButton = self.$("button[aria-label='Next month']").as("Next month button"),
            calendarHeader = self.$("[class*='MuiPickersCalendarHeader-label ']").as("Calendar header"),
            yearsListContainer = self.$("div[class*='MuiYearCalendar']").as("Calendar years container");

    private final ElementsCollection yearsList = yearsListContainer.$$("button").as("Calendar years list"),
            daysList = self.$$("[class*='monthContainer'] button").as("Calendar days list");

    public CalendarComponent(SelenideElement self) {
        super(self);
    }

    private static Calendar getCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public void selectDateInCalendar(Date date) {

        validateDate(date);
        Calendar calendar = getCalendar(date);
        var dateVal = new SimpleDateFormat("MM/dd/yyyy");

        log.info("Select date in calendar: [%s]", dateVal);
        Allure.step("Select date in calendar: [%s]".formatted(dateVal), () -> {
            selectYear(calendar.get(Calendar.YEAR));
            selectMonth(calendar.get(Calendar.MONTH));
            selectDay(calendar.get(Calendar.DAY_OF_MONTH));
            closeCalendar();
        });

    }

    private void validateDate(Date date) {
        if (date.before(MIN_DATE) && date.after(MAX_DATE)) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            throw new InvalidDateException("Available dates from [" + sdf.format(MIN_DATE) + "] to [" + sdf.format(MAX_DATE) + "]");
        }
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
                calendarTypeSwitchButton.shouldBe(clickable).click();
                // INFO: wait for switch calendar animation ends
                SelenideElement containerIdentifierElement = (calendarType == CalendarType.CALENDAR)
                        ? moveForwardButton
                        : yearsListContainer;
                containerIdentifierElement.shouldBe(visible);
            });
        }
    }

    @Step("Close calendar")
    private void closeCalendar() {
        if (self.is(exist, Duration.ofSeconds(2)))
            self.pressEscape();
    }

    @SuppressWarnings("unused")
    private int getCalendarYear() {
        return Integer.parseInt(calendarHeader.getText().split(" ")[1]);
    }

    @Step("Select year = [{year}]")
    private void selectYear(int year) {
        switchCalendarType(CalendarType.YEAR);
        yearsList.find(text(String.valueOf(year))).scrollIntoView(false).shouldBe(clickable).click();
    }

    private int getCalendarMonth() {
        return Month.valueOf(calendarHeader.getText().split(" ")[0]).getValue();
    }

    @Step("Select month = [{month}]")
    private void selectMonth(int month) {

        switchCalendarType(CalendarType.CALENDAR);
        var delta = month - getCalendarMonth();

        if (delta != 0) {

            SelenideElement moveElement = (delta > 0) ? moveForwardButton : moveBackButton;
            delta = Math.abs(delta);

            for (int steps = 0; steps < delta; steps++)
                moveElement.shouldBe(clickable).click();

            log.info("Selected month with number = [{}]", month);
            return;
        }
        log.info("Month with number = [{}] is already selected", month);

    }

    @Step("Select day = [{day}]")
    private void selectDay(int day) {
        daysList.find(attribute("aria-colindex", String.valueOf(day))).shouldBe(clickable).click();
    }

    private enum CalendarType {
        CALENDAR, YEAR
    }

}