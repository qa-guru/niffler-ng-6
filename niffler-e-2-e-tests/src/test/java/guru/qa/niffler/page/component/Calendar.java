package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.time.Month;
import java.util.Date;
import java.util.GregorianCalendar;

import static java.util.Calendar.*;

public class Calendar extends BaseComponent<Calendar> {

  private final SelenideElement input;
  private final SelenideElement calendarButton;
  private final SelenideElement prevMonthButton;
  private final SelenideElement nextMonthButton;
  private final SelenideElement currentMonthAndYear;
  private final ElementsCollection dateRows;

  public Calendar(SelenideElement self, SelenideDriver driver) {
    super(self, driver);
    this.input = driver.$("input[name='date']");
    this.calendarButton = driver.$("button[aria-label*='Choose date']");
    this.prevMonthButton = self.$("button[title='Previous month']");
    this.nextMonthButton = self.$("button[title='Next month']");
    this.currentMonthAndYear = self.$(".MuiPickersCalendarHeader-label");
    this.dateRows = self.$$(".MuiDayCalendar-weekContainer");
  }

  public Calendar(SelenideDriver driver) {
    super(driver.$(".MuiPickersLayout-root"), driver);
    this.input = driver.$("input[name='date']");
    this.calendarButton = driver.$("button[aria-label*='Choose date']");
    this.prevMonthButton = self.$("button[title='Previous month']");
    this.nextMonthButton = self.$("button[title='Next month']");
    this.currentMonthAndYear = self.$(".MuiPickersCalendarHeader-label");
    this.dateRows = self.$$(".MuiDayCalendar-weekContainer");
  }

  @Step("Select date in calendar: {date}")
  public void selectDateInCalendar(Date date) {
    java.util.Calendar cal = new GregorianCalendar();
    cal.setTime(date);
    calendarButton.click();
    final int desiredMonthIndex = cal.get(MONTH);
    selectYear(cal.get(YEAR));
    selectMonth(desiredMonthIndex);
    selectDay(cal.get(DAY_OF_MONTH));
  }

  private void selectYear(int expectedYear) {
    int actualYear = getActualYear();

    while (actualYear > expectedYear) {
      prevMonthButton.click();
      sleep(200);
      actualYear = getActualYear();
    }
    while (actualYear < expectedYear) {
      nextMonthButton.click();
      sleep(200);
      actualYear = getActualYear();
    }
  }

  private void selectMonth(int desiredMonthIndex) {
    int actualMonth = getActualMonthIndex();

    while (actualMonth > desiredMonthIndex) {
      prevMonthButton.click();
      sleep(200);
      actualMonth = getActualMonthIndex();
    }
    while (actualMonth < desiredMonthIndex) {
      nextMonthButton.click();
      sleep(200);
      actualMonth = getActualMonthIndex();
    }
  }

  private void selectDay(int desiredDay) {
    ElementsCollection rows = dateRows.snapshot();

    for (SelenideElement row : rows) {
      ElementsCollection days = row.$$("button").snapshot();
      for (SelenideElement day : days) {
        if (day.getText().equals(String.valueOf(desiredDay))) {
          day.click();
          return;
        }
      }
    }
  }

  private String getMonthNameByIndex(int mothIndex) {
    return Month.of(mothIndex + 1).name();
  }

  private int getActualMonthIndex() {
    return Month.valueOf(currentMonthAndYear.getText()
            .split(" ")[0]
            .toUpperCase())
        .ordinal();
  }

  private int getActualYear() {
    return Integer.parseInt(currentMonthAndYear.getText().split(" ")[1]);
  }
}
