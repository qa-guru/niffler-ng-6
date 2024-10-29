package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.BasePage;
import io.qameta.allure.Step;

import javax.annotation.ParametersAreNonnullByDefault;
import java.text.SimpleDateFormat;
import java.util.Date;

@ParametersAreNonnullByDefault
public class Calendar<T extends BasePage<?>> extends BaseComponent<T> {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    public Calendar(SelenideElement calendarInput, T page) {
        super(calendarInput, page);
    }

    @Step("Выбор даты в календаре: {date}")
    public T selectDateInCalendar(Date date) {
        String formattedDate = dateFormat.format(date);
        self.clear();
        self.setValue(formattedDate).pressEnter();
        return getPage();
    }
}